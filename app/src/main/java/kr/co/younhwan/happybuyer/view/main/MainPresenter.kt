package kr.co.younhwan.happybuyer.view.main

import android.Manifest
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class MainPresenter(
    private val view: MainContract.View,
    private val userData: UserRepository,
    private val productData: ProductRepository
) : MainContract.Model {

    override fun loadMainScreen() {
        // 딜레이
        SystemClock.sleep(1000)

        // 스플래쉬 화면 이후로 보여질 화면을 설정
        view.getAct().setTheme(R.style.Theme_HappyBuyer)
    }

    override fun requestPermission() {
        view.getAct().requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            ), 0
        )
    }

    override fun loadUser() {
        val app = ((view.getAct()).application) as GlobalApplication

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 (= 로그인 정보가 없을 때)
                app.isLogined = false
            } else if (tokenInfo != null) { // 토큰이 있을 때 (= 로그인 정보가 있을 때)
                app.isLogined = true
                app.kakaoAccountId = tokenInfo.id

                // oAuth key(kakaoAccountId) 를 이용해 사용자 정보를 DB 에서 가져온다.
                userData.readUser(app.kakaoAccountId!!, object : UserSource.readUserCallback {
                    override fun onReadUser(userItem: UserItem?) {
                        // Application 에 사용자 정보를 업데이트한다.
                        app.nickname = userItem?.nickname // 유저 이름, 닉네임
                        app.pointNumber = userItem?.pointNumber // 유저 포인트 번호
                        app.shippingAddress = userItem?.shippingAddress // 유저 주소
                        app.activatedBasket = userItem?.activatedBasket // 유저 장바구니 활성화 여부 (activate, deactivate, null)

                        if (app.activatedBasket == "activate"){
                            productData.getBasketProducts(
                                view.getAct(),
                                app.kakaoAccountId!!,
                                object : ProductSource.LoadBasketProductCallback {
                                    override fun onLoadBasketProducts(list: ArrayList<ProductItem>) {

                                    }
                                })
                        }
                    }
                })
            }
        }
    }
}