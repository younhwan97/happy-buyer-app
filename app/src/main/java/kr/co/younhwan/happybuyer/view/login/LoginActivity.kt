package kr.co.younhwan.happybuyer.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.ActivityLoginBinding
import kr.co.younhwan.happybuyer.view.splash.SplashActivity

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var viewDataBinding: ActivityLoginBinding

    private val loginPresenter: LoginPresenter by lazy {
        LoginPresenter(
            view = this,
            userData = UserRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 툴바
        viewDataBinding.loginToolbar.setNavigationOnClickListener {
            finish()
        }

        // 카카오 로그인 버튼
        viewDataBinding.kakaoLoginBtn.setOnClickListener {
            loginPresenter.loginWithKakao(this)
        }
    }

    override fun loginWithKakaoCallback(isSuccess: Boolean) {
        if (isSuccess) {
            // 백스택에 존재하는 엑티비티를 모두 제거후 메인 엑티비티를 다시 생성
            val splashIntent = Intent(this, SplashActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(splashIntent)
        } else {
            Snackbar.make(
                viewDataBinding.root,
                "로그인에 실패하였습니다.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}