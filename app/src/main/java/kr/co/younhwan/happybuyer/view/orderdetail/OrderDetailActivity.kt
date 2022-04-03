package kr.co.younhwan.happybuyer.view.orderdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.adapter.orderproduct.OrderAdapter
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderDetailBinding

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
            finish()
        } else {
            orderDetailPresenter.loadOrderDetail(orderInfo.orderId)

            // 툴바
            viewDataBinding.orderDetailToolbar.setNavigationOnClickListener {
                finish()
            }

            // 주문 번호
            viewDataBinding.orderDetailId.text = orderInfo.orderId.toString()

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
            if (orderInfo.payment == "만나서 현금결제") {
                viewDataBinding.orderDetailPaymentRadioOptionCash.isChecked = true
                viewDataBinding.orderDetailPaymentRadioOptionCard.isChecked = false
            } else {
                viewDataBinding.orderDetailPaymentRadioOptionCash.isChecked = false
                viewDataBinding.orderDetailPaymentRadioOptionCard.isChecked = true
            }
            viewDataBinding.orderDetailPaymentRadioOptionCard.isEnabled = false
            viewDataBinding.orderDetailPaymentRadioOptionCash.isEnabled = false

            // 배달정보
            viewDataBinding.orderDetailAddressReceiver.text = orderInfo.receiver
            viewDataBinding.orderDetailAddressPhone.text = orderInfo.phone
            viewDataBinding.orderDetailAddress.text = orderInfo.address

            // 배달 요청사항
            viewDataBinding.orderDetailRequirement.editText?.text = Editable.Factory.getInstance().newEditable(orderInfo.requirement)
            viewDataBinding.orderDetailRequirement.editText?.isEnabled = false

            viewDataBinding.orderDetailPoint.editText?.text = Editable.Factory.getInstance().newEditable(orderInfo.point)
            viewDataBinding.orderDetailPoint.editText?.isEnabled = false

            if(orderInfo.detectiveHandlingMethod == "주문 전체 취소"){
                viewDataBinding.orderDetailDefectiveHandlingOption1.isChecked = false
                viewDataBinding.orderDetailDefectiveHandlingOption2.isChecked = true
            } else {
                viewDataBinding.orderDetailDefectiveHandlingOption1.isChecked = true
                viewDataBinding.orderDetailDefectiveHandlingOption2.isChecked = false
            }
            viewDataBinding.orderDetailDefectiveHandlingOption1.isEnabled= false
            viewDataBinding.orderDetailDefectiveHandlingOption2.isEnabled = false
        }
    }

    override fun getView() = this

    override fun loadOrderDetailCallback(isSuccess: Boolean) {
        if (isSuccess) {
            viewDataBinding.orderDetailView.visibility = View.VISIBLE
        } else {

        }

        // 로딩 뷰 종료
        viewDataBinding.orderDetailLoadingView.visibility = View.GONE
        viewDataBinding.orderDetailLoadingImage.pauseAnimation()
    }
}