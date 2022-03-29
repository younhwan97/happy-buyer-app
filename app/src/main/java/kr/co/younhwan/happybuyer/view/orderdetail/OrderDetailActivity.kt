package kr.co.younhwan.happybuyer.view.orderdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity(), OrderDetailContract.View {
    lateinit var viewDataBinding: ActivityOrderDetailBinding

    private val orderDetailPresenter: OrderDetailPresenter by lazy {
        OrderDetailPresenter(
            view = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)
    }
}