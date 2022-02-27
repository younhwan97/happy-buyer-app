package kr.co.younhwan.happybuyer.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.databinding.ActivityLoginBinding
import kr.co.younhwan.happybuyer.view.login.presenter.LoginContract
import kr.co.younhwan.happybuyer.view.login.presenter.LoginPresenter
import kr.co.younhwan.happybuyer.view.splash.SplashActivity

class LoginActivity : AppCompatActivity(), LoginContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: ActivityLoginBinding

    /* Presenter */
    private val loginPresenter: LoginPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        LoginPresenter(
            this,
            userData = UserRepository,
        )
    }

    /* Method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 액션바 -> 툴바
        setSupportActionBar(viewDataBinding.toolbar)
        supportActionBar?.run {
            title = ""
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        // set event listener
        viewDataBinding.kakaoLoginBtn.setOnClickListener {
            loginPresenter.loginWithKakao(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun loginResultCallback(success: Boolean) {
        if (success) {
            // BackStack에 존재하는 Activity를 모두 제거후 Main Activity를 다시 생성
            val splashIntent = Intent(this, SplashActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(splashIntent)
        } else
            Snackbar.make(viewDataBinding.root, "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
    }
}