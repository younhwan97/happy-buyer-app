package kr.co.younhwan.happybuyer.view.splash

import android.util.Log
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedSource

class SplashPresenter(
    private val view: SplashContract.View,
    private val userData: UserRepository,
    private val wishedData: WishedRepository
) : SplashContract.Model {

    override fun loadUserInfo() {
        val app = view.getAct().application as GlobalApplication

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 토큰이 없을 때 (= 로그인 정보가 없을 때)
                app.isLogined = false
                view.finishSplashAct()
            } else if (tokenInfo != null) {
                // 토큰이 있을 때 (= 로그인 정보가 있을 때)
                app.isLogined = true
                app.kakaoAccountId = tokenInfo.id!!

                // oAuth key(= kakaoAccountId)를 이용해 사용자 정보를 DB 에서 가져온다.
                userData.read(
                    kakaoAccountId = app.kakaoAccountId,
                    readCallback = object : UserSource.ReadCallback {
                        override fun onRead(userItem: UserItem?) {
                            if (userItem != null && userItem.kakaoAccountId != -1L) {
                                // 유저 정보를 성공적으로 읽어왔을 때
                                // 사용자 정보 업데이트
                                app.nickname = userItem.nickname
                                app.point = userItem.point
                                app.activatedBasket = userItem.activatedBasket

                                // 사용자의 찜 목록을 읽어온다.
                                wishedData.readProducts(
                                    kakaoAccountId = app.kakaoAccountId,
                                    readProductsCallback = object :
                                        WishedSource.ReadProductsCallback {
                                        override fun onReadProducts(list: ArrayList<ProductItem>) {
                                            val productIdList = ArrayList<Int>()

                                            for (item in list) {
                                                productIdList.add(item.productId)
                                            }

                                            app.wishedProductId = productIdList

                                            view.finishSplashAct()
                                        }
                                    }
                                )
                            } else {
                                // 유저 정보를 읽어오는데 실패했을 때
                                view.finishSplashAct()
                            }
                        }
                    })
            }
        }
    }
}