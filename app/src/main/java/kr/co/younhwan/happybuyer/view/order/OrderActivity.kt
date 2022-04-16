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

        // 어플리케이션
        val app = application as GlobalApplication

        // 로딩 뷰 셋팅
        viewDataBinding.orderView.visibility = View.GONE
        viewDataBinding.orderBottomBtnContainer.visibility = View.GONE
        viewDataBinding.orderLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderLoadingImage.playAnimation()

        // 인텐트에서 데이터 추출
        val orderProducts = if (intent.hasExtra("selected_item_list")) {
            intent.getParcelableArrayListExtra<BasketItem>("selected_item_list")
        } else {
            null
        }

        if (orderProducts == null) {
            // 장바구니에서 고객이 선택한 아이템 정보가 넘어오지 않았을 때
            finish()
        } else {
            // 장바구니에서 고객이 선택한 아이템 정보가 정상적으로 넘어왔을 때
            // 기본 배송지 셋팅
            orderPresenter.loadDefaultAddress()

            // 장바구니에서 넘어온 상품의 유효성 검사 및 셋팅
            orderPresenter.setOrderProducts(orderProducts)

            // 툴바
            viewDataBinding.orderToolbar.setNavigationOnClickListener {
                finish()
            }

            // 배달정보
            val startAddAddressActForResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        // 로딩 뷰 셋팅
                        viewDataBinding.orderView.visibility = View.GONE
                        viewDataBinding.orderBottomBtnContainer.visibility = View.GONE
                        viewDataBinding.orderLoadingView.visibility = View.VISIBLE
                        viewDataBinding.orderLoadingImage.playAnimation()
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
                            val newAddress = it.data?.getParcelableExtra<AddressItem>("address")

                            runOnUiThread {
                                viewDataBinding.orderAddressReceiver.text =
                                    newAddress?.addressReceiver
                                viewDataBinding.orderAddress.text = newAddress?.address
                                viewDataBinding.orderAddressPhone.text = newAddress?.addressPhone
                            }

                            Snackbar.make(
                                viewDataBinding.root,
                                "주소가 변경되었습니다.",
                                Snackbar.LENGTH_SHORT
                            )
                        } else {
                            orderPresenter.loadDefaultAddress()

                            Snackbar.make(
                                viewDataBinding.root,
                                "알 수 없는 에러로 주소가 변경되지 않았습니다. ",
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

            // 배달 요청사항
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

            // 결제수단
            viewDataBinding.orderPaymentRadioOptionCard.isChecked = true
            viewDataBinding.orderPaymentRadioOptionCash.isChecked = false

            // 주문 상품 확인
            viewDataBinding.orderProductRecycler.adapter = orderAdapter
            viewDataBinding.orderProductRecycler.layoutManager =
                object : LinearLayoutManager(this) {
                    override fun canScrollHorizontally() = false
                    override fun canScrollVertically() = false
                }
            viewDataBinding.orderProductRecycler.addItemDecoration(orderAdapter.RecyclerDecoration())

            // 주문 버튼
            viewDataBinding.orderBtn.isEnabled = true
            viewDataBinding.orderBtn.setOnClickListener {
                // 다이얼로그 시작
                orderDialog.isCancelable = false
                orderDialog.show(supportFragmentManager, "order_dialog")

                // 중복 주문 생성 방지
                it.isEnabled = false

                // 주문정보
                val status = "주문접수"
                val name = if (orderProducts.size == 1) {
                    orderProducts[0].productName
                } else {
                    "${orderProducts[0].productName} 외 ${orderProducts.size - 1}건"
                }

                // 배달정보
                val receiver = viewDataBinding.orderAddressReceiver.text.toString()
                val phone = viewDataBinding.orderAddressPhone.text.toString()
                val address = viewDataBinding.orderAddress.text.toString()

                // 요청사항
                val requirement = viewDataBinding.orderRequirement.editText?.text.toString()
                val point = viewDataBinding.orderPointNumber.editText?.text.toString()
                val defectiveHandlingMethod =
                    if (viewDataBinding.orderDefectiveHandlingOption1.isChecked) {
                        viewDataBinding.orderDefectiveHandlingOption1.text.toString()
                    } else {
                        viewDataBinding.orderDefectiveHandlingOption2.text.toString()
                    }

                // 결제수단
                val payment = if (viewDataBinding.orderPaymentRadioOptionCard.isChecked) {
                    viewDataBinding.orderPaymentRadioOptionCard.text.toString()
                } else {
                    viewDataBinding.orderPaymentRadioOptionCash.text.toString()
                }

                // 주문상품
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

    override fun getAct() = this

    override fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?) {
        if (defaultAddressItem != null) {
            // 기본 배송지가 존재할 경우
            viewDataBinding.orderAddressContent.visibility = View.VISIBLE
            viewDataBinding.orderAddressEmptyContent.visibility = View.GONE

            viewDataBinding.orderAddressReceiver.text = defaultAddressItem.addressReceiver
            viewDataBinding.orderAddress.text = defaultAddressItem.address
            viewDataBinding.orderAddressPhone.text = defaultAddressItem.addressPhone

            viewDataBinding.orderBtn.isEnabled = true
        } else {
            // 기본 배송지가 존재하지 않을 경우
            viewDataBinding.orderAddressContent.visibility = View.GONE
            viewDataBinding.orderAddressEmptyContent.visibility = View.VISIBLE

            viewDataBinding.orderBtn.isEnabled = false
        }

        if (completeAsyncTask) {
            // 로딩 뷰 종료
            viewDataBinding.orderView.visibility = View.VISIBLE
            viewDataBinding.orderBottomBtnContainer.visibility = View.VISIBLE
            viewDataBinding.orderLoadingView.visibility = View.GONE
            viewDataBinding.orderLoadingImage.pauseAnimation()
        } else {
            completeAsyncTask = true
        }
    }

    override fun setOrderProductsCallback(isSuccess: Boolean) {
        if (isSuccess) {
            if (completeAsyncTask) {
                // 로딩 뷰 종료
                viewDataBinding.orderView.visibility = View.VISIBLE
                viewDataBinding.orderBottomBtnContainer.visibility = View.VISIBLE
                viewDataBinding.orderLoadingView.visibility = View.GONE
                viewDataBinding.orderLoadingImage.pauseAnimation()
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

        viewDataBinding.orderBtn.text = decimal.format(totalPrice).plus("원 주문하기")
    }

    override fun createOrderCallback(orderItem: OrderItem) {
        // 다이얼로그 종료
        orderDialog.isCancelable = true
        orderDialog.dismiss()

        if (orderItem.orderId == -1) {
            // 주문이 실패했을 때
            val snack = Snackbar.make(viewDataBinding.root, "주문에 실패했습니다.", Snackbar.LENGTH_LONG)
            snack.setAnchorView(R.id.orderBottomBtnContainer)
            snack.show()
        } else {
            // 주문이 성공적으로 완료되었을 때
            val orderSuccessIntent = Intent(this, OrderSuccessActivity::class.java)
            orderSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            orderSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            orderSuccessIntent.putExtra("order", orderItem)
            startActivity(orderSuccessIntent)
        }
    }
}