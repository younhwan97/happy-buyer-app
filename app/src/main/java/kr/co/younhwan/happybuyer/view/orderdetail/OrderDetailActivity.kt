package kr.co.younhwan.happybuyer.view.orderdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        if (intent.hasExtra("order")) {
            val order = intent.getParcelableExtra<OrderItem>("order")
            if(order != null){
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
}