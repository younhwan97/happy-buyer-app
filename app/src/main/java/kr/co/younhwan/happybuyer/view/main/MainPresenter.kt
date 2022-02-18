package kr.co.younhwan.happybuyer.view.main

import android.Manifest
import android.os.SystemClock
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class MainPresenter(
    private val view: MainContract.View,
    private val userData: UserRepository
) : MainContract.Model {

    override fun loadMainScreen(act: MainActivity) {
        SystemClock.sleep(1000)

        // 스플래쉬 화면 이후로 보여질 화면을 설정
        act.setTheme(R.style.Theme_HappyBuyer)
    }

    override fun requestPermission(act: MainActivity) {
        act.requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            ), 0
        )
    }

    override fun loadUser(app: GlobalApplication) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 (= 로그인 정보가 없을 때)
                app.isLogined = false // 로그인 상태 x

            } else if (tokenInfo != null) {
                app.isLogined = true // 로그인 상태 o
                app.kakaoAccountId = tokenInfo.id

                // oAuth key(kakaoAccountId) 를 이용해 사용자 정보를 DB 에서 가져온다.
                userData.readUser(app.kakaoAccountId!!, object : UserSource.readUserCallback {
                    override fun onReadUser(userItem: UserItem?) {
                        // Application 에 사용자 정보를 업데이트한다.
                        app.nickname = userItem?.nickname
                        app.pointNumber = userItem?.pointNumber
                        app.shippingAddress = userItem?.shippingAddress
                    }
                })
            }
        }
    }
}