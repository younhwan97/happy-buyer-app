package kr.co.younhwan.happybuyer.view.login.presenter

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class LoginPresenter(
    private val view: LoginContract.View,
    private val userData: UserRepository,
) : LoginContract.Model {

    private val loginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            view.loginResultCallback(false)
        } else if (token != null) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    view.loginResultCallback(false)
                } else if (user != null) {
                    val kakaoAccountId: Long = user.id!!
                    val kakaoAccountNickname: String? = user.kakaoAccount?.profile?.nickname

                    userData.createUser(
                        kakaoAccountId,
                        kakaoAccountNickname,
                        object : UserSource.createUserCallback {
                            override fun onCreateUser(isSuccess: Boolean) {
                                view.loginResultCallback(isSuccess)
                            }
                        })
                }
            }
        }
    }

    override fun loginWithKakao(context: Context) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡이 설치되어 있을 때
            UserApiClient.instance.loginWithKakaoTalk(context, callback = loginCallback)
        } else {
            // 카카오톡이 설치되지 않았을 때
            UserApiClient.instance.loginWithKakaoAccount(context, callback = loginCallback)
        }
    }
}