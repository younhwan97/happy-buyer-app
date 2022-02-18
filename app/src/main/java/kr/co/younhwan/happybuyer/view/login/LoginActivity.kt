package kr.co.younhwan.happybuyer.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.databinding.ActivityLoginBinding
import kr.co.younhwan.happybuyer.view.login.presenter.LoginContract
import kr.co.younhwan.happybuyer.view.login.presenter.LoginPresenter

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
            Toast.makeText(this, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
            val mainIntent = Intent(this, MainActivity::class.java)

            // BackStack 에 존재하는 Activity 를 모두 제거후 Main Activity 를 다시 생성
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent)
        } else {
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}