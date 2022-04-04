package kr.co.younhwan.happybuyer.view.ordersuccess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.younhwan.happybuyer.data.OrderItem
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

        val orderInfo = if (intent.hasExtra("order")) {
            intent.getParcelableExtra<OrderItem>("order")
        } else {
            null
        }

        // 툴바
        viewDataBinding.orderSuccessToolbar.setNavigationOnClickListener {
            finish()
        }

        // Success Image
        viewDataBinding.orderSuccessImage.playAnimation()

        viewDataBinding.orderSuccessImage.setOnClickListener {
            viewDataBinding.orderSuccessImage.playAnimation()
        }

        if (orderInfo == null) {
            viewDataBinding.orderSuccessDetailBtn.visibility = View.GONE
        } else {
            viewDataBinding.orderSuccessBePaidPrice.text = orderInfo.bePaidPrice
        }
    }
}