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
import kr.co.younhwan.happybuyer.view.order.adapter.OrderAdapter
import kr.co.younhwan.happybuyer.view.ordersuccess.OrderSuccessActivity
import java.text.DecimalFormat

class OrderActivity : AppCompatActivity(), OrderContract.View {
    lateinit var viewDataBinding: ActivityOrderBinding

    private val orderPresenter: OrderPresenter by lazy {
        OrderPresenter(
            view = this,
            addressData = AddressRepository,
            basketData = BasketRepository,
            orderData = OrderRepository,
            orderAdapterModel = orderAdapter,
            orderAdapterView = orderAdapter
        )
    }

    private val orderAdapter: OrderAdapter by lazy {
        OrderAdapter()
    }

    private var orderProducts = ArrayList<BasketItem>()

    override fun onResume() {
        super.onResume()
        orderPresenter.setOrderProduct(orderProducts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        if (intent.hasExtra("selected_item_list")) {
            // 장바구니에서 고객이 선택한 아이템 정보가 정상적으로 넘어왔을 때
            val selectedItemList =
                intent.getParcelableArrayListExtra<BasketItem>("selected_item_list")

            if (selectedItemList != null) {
                for (item in selectedItemList) {
                    orderProducts.add(item)
                }

                orderPresenter.loadDefaultAddress()
                orderPresenter.setOrderProduct(orderProducts)
            } else {
                setResult(RESULT_CANCELED)
                finish()
            }
        } else {
            // 장바구니에서 고객이 선택한 아이템 정보가 넘어오지 않았을 때
            setResult(RESULT_CANCELED)
            finish()
        }

        // 툴바
        viewDataBinding.orderToolbar.setNavigationOnClickListener {
            finish()
        }

        // 배달정보
        val startAddAddressActForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
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
                            viewDataBinding.orderAddressReceiver.text = newAddress?.addressReceiver
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

                    snack.setAnchorView(R.id.orderBtnContainer)
                    snack.show()
                }
            }

        viewDataBinding.orderAddressSelectBtn.setOnClickListener {
            val addressIntent = Intent(this, AddressActivity::class.java)
            addressIntent.putExtra("is_select_mode", true)
            startAddressActForResult.launch(addressIntent)
        }

        // 배달 요청사항
        viewDataBinding.orderPointNumber.editText?.addTextChangedListener(object :
            PhoneNumberFormattingTextWatcher("KR") {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                super.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
            }
        })
        viewDataBinding.orderDefectiveHandlingOption1.isChecked = true
        viewDataBinding.orderDefectiveHandlingOption2.isChecked = false

        // 결제수단
        viewDataBinding.orderPaymentRadioOptionCash.isChecked = false
        viewDataBinding.orderPaymentRadioOptionCard.isChecked = true

        // 주문 상품 확인
        viewDataBinding.orderProductRecycler.adapter = orderAdapter
        viewDataBinding.orderProductRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.orderProductRecycler.addItemDecoration(orderAdapter.RecyclerDecoration())

        // 주문 버튼
        viewDataBinding.orderBtn.isEnabled = true
        viewDataBinding.orderBtn.setOnClickListener {
            // 중복 주문 생성 방지
            it.isEnabled = false

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
    }

    override fun setOrderProductCallback(isSuccess: Boolean) {
        if (!isSuccess) {
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

    override fun createOrderCallback(orderId: Int) {
        val orderSuccessIntent = Intent(this, OrderSuccessActivity::class.java)
        orderSuccessIntent.putExtra("order_id", orderId)
        orderSuccessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(orderSuccessIntent)
    }
}