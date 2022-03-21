package kr.co.younhwan.happybuyer.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.databinding.ActivitySplashBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity

class SplashActivity : AppCompatActivity(), SplashContract.View {
    /* View Binding */
    lateinit var viewDataBinding: ActivitySplashBinding

    /* Presenter */
    private val splashPresenter by lazy {
        SplashPresenter(
            view = this,
            userData = UserRepository,
            productData = ProductRepository,
            wishedData = WishedRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create binding object
        viewDataBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // Lottie 애니메이션 플레이
        viewDataBinding.splashImage.playAnimation()

        // 앱을 사용하는데 반드시 필요한 데이터를 로드
        splashPresenter.loadUserInfo() // 유저 정보 (닉네임, 회원 번호, 포인트 번호, 찜 리스트, 장바구니 상품의 개수 ..)
    }

    override fun finishSplashActivity() {
        // 유저 정보가 모두 로드되면 호출
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            viewDataBinding.splashImage.pauseAnimation()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2200)
    }

    override fun getAct(): SplashActivity = this
}