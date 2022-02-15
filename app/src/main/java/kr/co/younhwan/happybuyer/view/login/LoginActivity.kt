package kr.co.younhwan.happybuyer.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    // View Binding
    private lateinit var loginActivityBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)

        // 액션바 -> 툴바
        setSupportActionBar(loginActivityBinding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("kakao", "로그인 실패", error)
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                val mainIntent = Intent(this, MainActivity::class.java)
                // BackStack에 존재하는 Activity를 모두 제거후 Main Activity를 다시 생성
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        loginActivityBinding.kakaoLoginBtn.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                // 카카오톡이 설치되어 있을 때
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                // 카카오톡이 설치되지 않았을 때
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}