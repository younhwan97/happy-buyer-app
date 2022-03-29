package kr.co.younhwan.happybuyer.view.ordersuccess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.younhwan.happybuyer.databinding.ActivityOrderSuccessBinding

class OrderSuccessActivity : AppCompatActivity(), OrderSuccessContract.View {
    lateinit var viewDataBinding: ActivityOrderSuccessBinding

    private val orderSuccessPresenter: OrderSuccessPresenter by lazy {
        OrderSuccessPresenter(
            view = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        if(intent.hasExtra("order_id")){

        }
    }
}