package kr.co.younhwan.happybuyer.view.orderhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity(), OrderHistoryContract.View {
    lateinit var viewDataBinding: ActivityOrderHistoryBinding

    private val orderHistoryPresenter: OrderHistoryPresenter by lazy {
        OrderHistoryPresenter(
            view = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)
    }
}