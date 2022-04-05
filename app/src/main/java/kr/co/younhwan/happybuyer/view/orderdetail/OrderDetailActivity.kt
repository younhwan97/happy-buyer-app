package kr.co.younhwan.happybuyer.view.orderdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.adapter.orderproduct.OrderAdapter
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderDetailBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity

class OrderDetailActivity : AppCompatActivity(), OrderDetailContract.View {
    lateinit var viewDataBinding: ActivityOrderDetailBinding

    private val orderDetailPresenter: OrderDetailPresenter by lazy {
        OrderDetailPresenter(
            view = this,
            orderData = OrderRepository,
            orderAdapterModel = orderAdapter,
            orderAdapterView = orderAdapter
        )
    }

    private val orderAdapter: OrderAdapter by lazy {
        OrderAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 로딩 뷰 셋팅
        viewDataBinding.orderDetailView.visibility = View.GONE
        viewDataBinding.orderDetailLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderDetailLoadingImage.playAnimation()

        // 인텐트에서 데이터 추출
        val orderInfo = if (intent.hasExtra("order")) {
            intent.getParcelableExtra<OrderItem>("order")
        } else {
            null
        }

        if (orderInfo == null) {
            // 메인(주문 내역 프래그먼트), 주문성공 엑티비티에서 주문 정보를 전달받지 못했을 때
            finishAct()
        } else {
            // 메인(주문 내역 프래그먼트), 주문성공 엑티비티에서 주문 정보를 전달받았을 때
            // 주문번호를 이용해 주문한 상품을 로드
            orderDetailPresenter.loadOrderProducts(orderInfo.orderId)

            // 툴바
            viewDataBinding.orderDetailToolbar.setNavigationOnClickListener {
                finishAct()
            }

            // 주문 번호 및 주문 상태
            viewDataBinding.orderDetailId.text = orderInfo.orderId.toString()
            viewDataBinding.orderDetailStatus.text = orderInfo.status
            viewDataBinding.orderDetailDate.text = "(".plus(orderInfo.date).plus(")")

            // 주문 상품
            viewDataBinding.orderDetailProductRecycler.adapter = orderAdapter
            viewDataBinding.orderDetailProductRecycler.layoutManager =
                object : LinearLayoutManager(this) {
                    override fun canScrollHorizontally() = false
                    override fun canScrollVertically() = false
                }
            viewDataBinding.orderDetailProductRecycler.addItemDecoration(orderAdapter.RecyclerDecoration())

            // 가격
            viewDataBinding.orderDetailOriginalPrice.text = orderInfo.originalPrice
            viewDataBinding.orderDetailEventPrice.text = orderInfo.eventPrice
            viewDataBinding.orderDetailBePaidPrice.text = orderInfo.bePaidPrice

            // 결제 수단
            when (orderInfo.payment.replace(" ", "")) {
                "만나서 현금결제".replace(" ", "") -> {
                    viewDataBinding.orderDetailPaymentRadioOptionCash.isChecked = true
                    viewDataBinding.orderDetailPaymentRadioOptionCard.isChecked = false
                }

                "만나서 카드결제".replace(" ", "") -> {
                    viewDataBinding.orderDetailPaymentRadioOptionCash.isChecked = false
                    viewDataBinding.orderDetailPaymentRadioOptionCard.isChecked = true
                }

                else -> {
                    finishAct()
                }
            }
            viewDataBinding.orderDetailPaymentRadioOptionCard.isEnabled = false
            viewDataBinding.orderDetailPaymentRadioOptionCash.isEnabled = false

            // 배달정보
            viewDataBinding.orderDetailAddressReceiver.text = orderInfo.receiver
            viewDataBinding.orderDetailAddressPhone.text = orderInfo.phone
            viewDataBinding.orderDetailAddress.text = orderInfo.address

            // 배달 요청사항
            viewDataBinding.orderDetailRequirement.editText?.text =
                Editable.Factory.getInstance().newEditable(orderInfo.requirement)
            viewDataBinding.orderDetailRequirement.editText?.isEnabled = false

            viewDataBinding.orderDetailPoint.editText?.text =
                Editable.Factory.getInstance().newEditable(orderInfo.point)
            viewDataBinding.orderDetailPoint.editText?.isEnabled = false

            // 결품발생 시 처리 방법
            when (orderInfo.detectiveHandlingMethod.replace(" ", "")) {
                "주문 전체 취소".replace(" ", "") -> {
                    viewDataBinding.orderDetailDefectiveHandlingOption1.isChecked = false
                    viewDataBinding.orderDetailDefectiveHandlingOption2.isChecked = true
                }

                "해당 상품 제외 후 배달".replace(" ", "") -> {
                    viewDataBinding.orderDetailDefectiveHandlingOption1.isChecked = true
                    viewDataBinding.orderDetailDefectiveHandlingOption2.isChecked = false
                }

                else -> {
                    finishAct()
                }
            }
            viewDataBinding.orderDetailDefectiveHandlingOption1.isEnabled = false
            viewDataBinding.orderDetailDefectiveHandlingOption2.isEnabled = false
        }
    }

    override fun onBackPressed() {
        finishAct()
    }

    private fun finishAct() {
        if (isTaskRoot) {
            // 현재 엑티비티가 첫번째 엑티비티 일 때 (= 백스택에 다른 엑티비티가 존재하지 않을 때)
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mainIntent.putExtra("init_frag", "order_history")
            startActivity(mainIntent)
        } else {
            // 현재 엑티비티가 첫번째 엑티비티가 아닐 때 (= 백스택에 다른 엑티비티가 존재할 때)
            finish()
        }
    }

    override fun getAct() = this

    override fun loadOrderProductsCallback(isSuccess: Boolean) {
        if (isSuccess) {
            // 성공적으로 디비에서 주문한 상품 목록을 로드했을 때
            viewDataBinding.orderDetailView.visibility = View.VISIBLE

            // 로딩 뷰 종료
            viewDataBinding.orderDetailLoadingView.visibility = View.GONE
            viewDataBinding.orderDetailLoadingImage.pauseAnimation()
        } else {
            // 디비에서 주문한 상품 목록을 로드하는데 실패했을 때
            finishAct()
        }
    }
}