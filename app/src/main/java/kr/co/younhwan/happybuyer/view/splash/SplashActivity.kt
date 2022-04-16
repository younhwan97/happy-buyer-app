package kr.co.younhwan.happybuyer.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.databinding.ActivitySplashBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity

class SplashActivity : AppCompatActivity(), SplashContract.View {
    lateinit var viewDataBinding: ActivitySplashBinding

    private val splashPresenter by lazy {
        SplashPresenter(
            view = this,
            userData = UserRepository,
            wishedData = WishedRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 로딩 뷰 셋팅
        viewDataBinding.splashImage.playAnimation()

        // 유저 정보 로드
        splashPresenter.loadUserInfo()
    }

    override fun getAct() = this

    override fun finishSplashAct() {
        // 로딩 뷰 종료
        viewDataBinding.splashImage.pauseAnimation()

        // 메인 엑티비티 실행
        startActivity(Intent(this, MainActivity::class.java))

        // 현재 엑티비티 제거
        finish()
    }
}