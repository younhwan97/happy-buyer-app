package kr.co.younhwan.happybuyer.view.ordersuccess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderSuccessBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.orderdetail.OrderDetailActivity

class OrderSuccessActivity : AppCompatActivity(), OrderSuccessContract.View {
    lateinit var viewDataBinding: ActivityOrderSuccessBinding

    private val orderSuccessPresenter: OrderSuccessPresenter by lazy {
        OrderSuccessPresenter(
            view = this,
            orderData = OrderRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 로딩 뷰 셋팅
        setLoadingView()

        // 인텐트에서 데이터 추출
        val orderInfo = if (intent.hasExtra("order")) {
            intent.getParcelableExtra<OrderItem>("order")
        } else {
            null
        }

        if (orderInfo == null) {
            // 주문 엑티비티에서 주문 정보를 정상적으로 전달받지 못했을 때
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mainIntent.putExtra("init_frag", "order_history")
            startActivity(mainIntent)
        } else {
            // 주문 엑티비티에서 주문 정보를 정상적으로 전달받았을 때
            // 유효성 검사 (전달받은 주문 정보와 디비에 기록된 정보를 비교)
            orderSuccessPresenter.checkValidation(orderInfo)

            // 툴바
            viewDataBinding.orderSuccessToolbar.setNavigationOnClickListener {
                // 백스택에 존재하는 모든 엑티비티를 지우고 메인 엑티비티로 이동
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainIntent)
            }

            // 성공 애니메이션
            viewDataBinding.orderSuccessImage.setOnClickListener {
                viewDataBinding.orderSuccessImage.playAnimation()
            }

            // 결제금액
            viewDataBinding.orderSuccessBePaidPrice.text = orderInfo.bePaidPrice

            // 하단 버튼
            viewDataBinding.orderSuccessCloseBtn.isClickable = true
            viewDataBinding.orderSuccessCloseBtn.setOnClickListener {
                it.isClickable = false

                // 백스택에 존재하는 모든 엑티비티를 지우고 메인 엑티비티로 이동
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainIntent)
            }

            viewDataBinding.orderSuccessDetailBtn.isClickable = true
            viewDataBinding.orderSuccessDetailBtn.setOnClickListener {
                it.isClickable = false

                // 백스택에 존재하는 모든 엑티비티를 지우고 메인 엑티비티의 주문내역 프래그먼트로 이동
                val orderDetailIntent = Intent(this, OrderDetailActivity::class.java)
                orderDetailIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                orderDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                orderDetailIntent.putExtra("order", orderInfo)
                startActivity(orderDetailIntent)
            }
        }
    }

    override fun onBackPressed() {
        // BACK 버튼을 눌렀을 때 메인 엑티비티로 이동하고 주문 성공 엑티비티를 백스택에서 제거
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)
    }

    private fun setLoadingView() {
        viewDataBinding.orderSuccessView.visibility = View.GONE
        viewDataBinding.orderSuccessBtnContainer.visibility = View.GONE
        viewDataBinding.orderSuccessLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderSuccessLoadingImage.playAnimation()
    }

    override fun getAct() = this

    override fun checkValidationCallback(isSuccess: Boolean) {
        if (isSuccess) {
            // 유효성 검사를 통과했을 때
            // 로딩 뷰 종료
            viewDataBinding.orderSuccessBtnContainer.visibility = View.VISIBLE
            viewDataBinding.orderSuccessView.visibility = View.VISIBLE
            viewDataBinding.orderSuccessLoadingView.visibility = View.GONE
            viewDataBinding.orderSuccessLoadingImage.pauseAnimation()

            // 성공 이미지 애니메이션
            viewDataBinding.orderSuccessImage.playAnimation()
        } else {
            // 유효성 검사를 통과하지 못했을 때
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mainIntent.putExtra("init_frag", "order_history")
            startActivity(mainIntent)
        }
    }
}