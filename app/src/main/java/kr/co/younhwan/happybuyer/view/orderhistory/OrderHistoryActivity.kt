package kr.co.younhwan.happybuyer.view.orderhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderHistoryBinding
import kr.co.younhwan.happybuyer.view.orderdetail.OrderDetailActivity
import kr.co.younhwan.happybuyer.view.orderhistory.adapter.OrderHistoryAdapter

class OrderHistoryActivity : AppCompatActivity(), OrderHistoryContract.View {
    lateinit var viewDataBinding: ActivityOrderHistoryBinding

    private val orderHistoryPresenter: OrderHistoryPresenter by lazy {
        OrderHistoryPresenter(
            view = this,
            orderData = OrderRepository,
            orderHistoryAdapterModel = orderHistoryAdapter,
            orderHistoryAdapterView = orderHistoryAdapter
        )
    }

    private val orderHistoryAdapter: OrderHistoryAdapter by lazy {
        OrderHistoryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        orderHistoryPresenter.loadOrderHistory()

        // 툴바
        viewDataBinding.orderHistoryToolbar.setNavigationOnClickListener {
            finish()
        }
        
        viewDataBinding.orderHistoryRecycler.adapter = orderHistoryAdapter
        viewDataBinding.orderHistoryRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.orderHistoryRecycler.addItemDecoration(orderHistoryAdapter.RecyclerDecoration())
    }

    override fun getView() = this

    override fun createOrderDetailAct(orderId: Int) {
        val orderDetailIntent = Intent(this, OrderDetailActivity::class.java)
        orderDetailIntent.putExtra("order_id", orderId)
        startActivity(orderDetailIntent)
    }
}