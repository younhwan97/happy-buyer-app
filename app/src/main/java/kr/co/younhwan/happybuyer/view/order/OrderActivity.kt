package kr.co.younhwan.happybuyer.view.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderBinding
import kr.co.younhwan.happybuyer.view.addeditaddress.AddAddressActivity
import kr.co.younhwan.happybuyer.view.address.AddressActivity
import kr.co.younhwan.happybuyer.adapter.orderproduct.OrderAdapter
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.view.order.dialog.OrderDialogFragment
import kr.co.younhwan.happybuyer.view.ordersuccess.OrderSuccessActivity
import java.text.DecimalFormat

class OrderActivity : AppCompatActivity(), OrderContract.View {
    lateinit var viewDataBinding: ActivityOrderBinding

    private val orderPresenter: OrderPresenter by lazy {
        OrderPresenter(
            view = this,
            addressData = AddressRepository,
            userData = UserRepository,
            basketData = BasketRepository,
            orderData = OrderRepository,
            orderAdapterModel = orderAdapter,
            orderAdapterView = orderAdapter
        )
    }

    private val orderAdapter: OrderAdapter by lazy {
        OrderAdapter()
    }

    private var completeAsyncTask = false
    private val orderDialog = OrderDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // ??????????????????
        val app = application as GlobalApplication

        // ?????? ??? ??????
        setLoadingView()

        // ??????????????? ????????? ??????
        val orderProducts = if (intent.hasExtra("selected_item_list")) {
            intent.getParcelableArrayListExtra<BasketItem>("selected_item_list")
        } else {
            null
        }

        if (orderProducts == null) {
            // ?????????????????? ????????? ????????? ????????? ????????? ???????????? ????????? ???
            finish()
        } else {
            // ?????????????????? ????????? ????????? ????????? ????????? ??????????????? ???????????? ???
            // ?????? ????????? ??????
            orderPresenter.loadDefaultAddress()

            // ?????????????????? ????????? ????????? ????????? ?????? ??? ??????
            orderPresenter.setOrderProducts(orderProducts)

            // ??????
            viewDataBinding.orderToolbar.setNavigationOnClickListener {
                finish()
            }

            // ????????????
            val startAddAddressActForResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        // ?????? ??? ??????
                        setLoadingView()

                        // ?????? ????????? ??????
                        completeAsyncTask = true
                        orderPresenter.loadDefaultAddress()
                    }
                }

            viewDataBinding.orderAddressAddBtn.setOnClickListener {
                val addAddressIntent = Intent(this, AddAddressActivity::class.java)
                startAddAddressActForResult.launch(addAddressIntent)
            }

            val startAddressActForResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        val snack = if (it.data?.hasExtra("address") == true) {
                            // ????????? ?????? ??????
                            val newAddress = it.data?.getParcelableExtra<AddressItem>("address")

                            // ?????? ??????
                            runOnUiThread {
                                viewDataBinding.orderAddressReceiver.text =
                                    newAddress?.addressReceiver
                                viewDataBinding.orderAddress.text = newAddress?.address
                                viewDataBinding.orderAddressPhone.text = newAddress?.addressPhone
                            }

                            // ????????? ??????
                            Snackbar.make(
                                viewDataBinding.root,
                                "????????? ?????????????????????.",
                                Snackbar.LENGTH_SHORT
                            )
                        } else {
                            orderPresenter.loadDefaultAddress()

                            // ????????? ??????
                            Snackbar.make(
                                viewDataBinding.root,
                                "??? ??? ?????? ????????? ????????? ???????????? ???????????????. ",
                                Snackbar.LENGTH_SHORT
                            )
                        }

                        snack.setAnchorView(R.id.orderBottomBtnContainer)
                        snack.show()
                    }
                }

            viewDataBinding.orderAddressSelectBtn.setOnClickListener {
                val addressIntent = Intent(this, AddressActivity::class.java)
                addressIntent.putExtra("is_select_mode", true)
                startAddressActForResult.launch(addressIntent)
            }

            // ????????????
            viewDataBinding.orderPaymentRadioOptionCard.isChecked = true
            viewDataBinding.orderPaymentRadioOptionCash.isChecked = false

            // ?????? ????????????
            if (app.point != null && app.point != "null")
                viewDataBinding.orderPointNumber.editText?.setText(app.point)

            viewDataBinding.orderPointNumber.editText?.addTextChangedListener(object :
                PhoneNumberFormattingTextWatcher("KR") {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    super.beforeTextChanged(s, start, count, after)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    super.onTextChanged(s, start, before, count)
                }
            })

            viewDataBinding.orderDefectiveHandlingOption1.isChecked = true
            viewDataBinding.orderDefectiveHandlingOption2.isChecked = false

            // ?????? ?????? ??????
            viewDataBinding.orderProductRecycler.adapter = orderAdapter
            viewDataBinding.orderProductRecycler.layoutManager =
                object : LinearLayoutManager(this) {
                    override fun canScrollHorizontally() = false
                    override fun canScrollVertically() = false
                }

            viewDataBinding.orderProductRecycler.addItemDecoration(orderAdapter.RecyclerDecoration())

            // ?????? ??????
            viewDataBinding.orderBtn.isEnabled = true
            viewDataBinding.orderBtn.setOnClickListener {
                // ?????? ?????? ?????? ??????
                it.isEnabled = false

                // ??????????????? ??????
                orderDialog.isCancelable = false
                orderDialog.show(supportFragmentManager, "order_dialog")

                // ????????????
                val name = if (orderProducts.size == 1) {
                    orderProducts[0].productName
                } else {
                    "${orderProducts[0].productName} ??? ${orderProducts.size - 1}???"
                }
                val status = "????????????"

                // ????????????
                val receiver = viewDataBinding.orderAddressReceiver.text.toString()
                val phone = viewDataBinding.orderAddressPhone.text.toString()
                val address = viewDataBinding.orderAddress.text.toString()

                // ????????????
                val requirement = viewDataBinding.orderRequirement.editText?.text.toString()
                val point = viewDataBinding.orderPointNumber.editText?.text.toString()
                val defectiveHandlingMethod =
                    if (viewDataBinding.orderDefectiveHandlingOption1.isChecked) {
                        viewDataBinding.orderDefectiveHandlingOption1.text.toString()
                    } else {
                        viewDataBinding.orderDefectiveHandlingOption2.text.toString()
                    }

                // ????????????
                val payment = if (viewDataBinding.orderPaymentRadioOptionCard.isChecked) {
                    viewDataBinding.orderPaymentRadioOptionCard.text.toString()
                } else {
                    viewDataBinding.orderPaymentRadioOptionCash.text.toString()
                }

                // ????????????
                val originalPrice = viewDataBinding.orderOriginalPrice.text.toString()
                val eventPrice = viewDataBinding.orderEventPrice.text.toString()
                val bePaidPrice = viewDataBinding.orderBePaidPrice.text.toString()

                orderPresenter.createOrder(
                    OrderItem(
                        orderId = -1,
                        name = name,
                        status = status,
                        date = null,
                        receiver = receiver,
                        phone = phone,
                        address = address,
                        requirement = requirement,
                        point = point,
                        detectiveHandlingMethod = defectiveHandlingMethod,
                        payment = payment,
                        originalPrice = originalPrice,
                        eventPrice = eventPrice,
                        bePaidPrice = bePaidPrice,
                        products = orderProducts
                    )
                )
            }
        }
    }

    private fun setLoadingView() {
        viewDataBinding.orderView.visibility = View.GONE
        viewDataBinding.orderBottomBtnContainer.visibility = View.GONE
        viewDataBinding.orderLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderLoadingImage.playAnimation()
    }

    override fun getAct() = this

    override fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?) {
        if (defaultAddressItem == null) {
            // ?????? ???????????? ???????????? ?????? ??????
            viewDataBinding.orderAddressContent.visibility = View.GONE
            viewDataBinding.orderAddressEmptyContent.visibility = View.VISIBLE

            viewDataBinding.orderBtn.isEnabled = false
        } else {
            // ?????? ???????????? ????????? ??????
            viewDataBinding.orderAddressContent.visibility = View.VISIBLE
            viewDataBinding.orderAddressEmptyContent.visibility = View.GONE

            viewDataBinding.orderAddressReceiver.text = defaultAddressItem.addressReceiver
            viewDataBinding.orderAddress.text = defaultAddressItem.address
            viewDataBinding.orderAddressPhone.text = defaultAddressItem.addressPhone
        }

        if (completeAsyncTask) {
            // ?????? ??? ??????
            viewDataBinding.orderView.visibility = View.VISIBLE
            viewDataBinding.orderBottomBtnContainer.visibility = View.VISIBLE
            viewDataBinding.orderLoadingView.visibility = View.GONE
            viewDataBinding.orderLoadingImage.pauseAnimation()

            // ?????? ?????? ?????????
            viewDataBinding.orderBtn.isEnabled = true
        } else {
            completeAsyncTask = true
        }
    }

    override fun setOrderProductsCallback(isSuccess: Boolean) {
        if (isSuccess) {
            if (completeAsyncTask) {
                // ?????? ??? ??????
                viewDataBinding.orderView.visibility = View.VISIBLE
                viewDataBinding.orderBottomBtnContainer.visibility = View.VISIBLE
                viewDataBinding.orderLoadingView.visibility = View.GONE
                viewDataBinding.orderLoadingImage.pauseAnimation()

                // ?????? ?????? ?????????
                viewDataBinding.orderBtn.isEnabled = true
            } else {
                completeAsyncTask = true
            }
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun calculatePriceCallback(
        totalPrice: Int,
        originalTotalPrice: Int,
        basketItemCount: Int
    ) {
        val decimal = DecimalFormat("#,###")

        viewDataBinding.orderOriginalPrice.text = decimal.format(originalTotalPrice)
        viewDataBinding.orderEventPrice.text = decimal.format(totalPrice - originalTotalPrice)
        viewDataBinding.orderBePaidPrice.text = decimal.format(totalPrice)

        viewDataBinding.orderBtn.text = decimal.format(totalPrice).plus("??? ????????????")
    }

    override fun createOrderCallback(orderItem: OrderItem) {
        // ??????????????? ??????
        orderDialog.isCancelable = true
        orderDialog.dismiss()

        if (orderItem.orderId == -1) {
            // ????????? ???????????? ???
            val snack = Snackbar.make(viewDataBinding.root, "????????? ??????????????????.", Snackbar.LENGTH_LONG)
            snack.setAnchorView(R.id.orderBottomBtnContainer)
            snack.show()
        } else {
            // ????????? ??????????????? ??????????????? ???
            val orderSuccessIntent = Intent(this, OrderSuccessActivity::class.java)
            orderSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            orderSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            orderSuccessIntent.putExtra("order", orderItem)
            startActivity(orderSuccessIntent)
        }
    }
}