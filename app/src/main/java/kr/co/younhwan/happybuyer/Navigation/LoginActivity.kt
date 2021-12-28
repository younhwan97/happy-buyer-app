package kr.co.younhwan.happybuyer.Navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityLoginBinding
import kr.co.younhwan.happybuyer.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    // View Binding
    lateinit var loginActivityBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("kakao", "로그인 실패", error)
            }
            else if (token != null) {
                Log.i("kakao", "로그인 성공 ${token.accessToken}")
                val main_intent = Intent(this, MainActivity::class.java)
                main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main_intent)
            }
        }


        loginActivityBinding.kakaoLoginBtn.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}