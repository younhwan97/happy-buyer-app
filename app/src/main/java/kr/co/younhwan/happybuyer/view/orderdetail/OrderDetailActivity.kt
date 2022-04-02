package kr.co.younhwan.happybuyer.view.orderdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity(), OrderDetailContract.View {
    lateinit var viewDataBinding: ActivityOrderDetailBinding

    private val orderDetailPresenter: OrderDetailPresenter by lazy {
        OrderDetailPresenter(
            view = this,
            orderData = OrderRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 로딩 뷰 셋팅
        viewDataBinding.orderDetailLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderDetailLoadingImage.playAnimation()

        // 인텐트에서 데이터 추출
        if (intent.hasExtra("order")) {
            val order = intent.getParcelableExtra<OrderItem>("order")
            if (order != null) {
                orderDetailPresenter.loadOrderDetail(order.orderId)
            }
        } else {
            finish()
        }

        // 툴바
        viewDataBinding.orderDetailToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun getView() = this

    override fun loadOrderDetailCallback(isSuccess: Boolean) {
        if(isSuccess){

        } else {
            
        }
        
        // 로딩 뷰 종료
        viewDataBinding.orderDetailLoadingView.visibility = View.GONE
        viewDataBinding.orderDetailLoadingImage.pauseAnimation()
    }
}