package kr.co.younhwan.happybuyer.view.splash

import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedSource

class SplashPresenter(
    private val view: SplashContract.View,
    private val userData: UserRepository,
    private val wishedData: WishedRepository,
    private val basketData: BasketRepository
) : SplashContract.Model {

    override fun loadUserInfo() {
        val app = view.getAct().application as GlobalApplication

        // 카카오 API를 이용해 유저의 토큰 정보를 읽어온다.
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 토큰이 없을 때 (= 로그인 정보가 없을 때)
                app.isLogined = false
                view.finishSplashAct()
            } else if (tokenInfo != null) {
                // 토큰이 있을 때 (= 로그인 정보가 있을 때)
                app.isLogined = true
                app.kakaoAccountId = tokenInfo.id ?: -1L

                // oAuth key(= kakaoAccountId)를 이용해 DB에 존재하는 유저 정보를 가져온다.
                userData.read(
                    kakaoAccountId = app.kakaoAccountId,
                    readCallback = object : UserSource.ReadCallback {
                        override fun onRead(userItem: UserItem?) {
                            if (userItem != null && userItem.kakaoAccountId != -1L) {
                                // 유저 정보를 읽어오는데 성공했을 때
                                // 유저 정보 업데이트
                                app.point = userItem.point
                                app.nickname = userItem.nickname

                                var completeLoadUserData = false

                                // (유저 아이디를 이용해) 유저의 찜 목록을 읽어온다.
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

                                            if (completeLoadUserData) {
                                                view.finishSplashAct()
                                            } else {
                                                completeLoadUserData = true
                                            }
                                        }
                                    }
                                )

                                // (유저 아이디를 이용해) 유저의 장바구니 상품 목록의 개수를 읽어온다.
                                basketData.readProducts(
                                    kakaoAccountId = app.kakaoAccountId,
                                    readProductsCallback = object :
                                        BasketSource.ReadProductsCallback {
                                        override fun onReadProducts(list: ArrayList<BasketItem>) {
                                            app.basketItemCount = list.size

                                            if (completeLoadUserData) {
                                                view.finishSplashAct()
                                            } else {
                                                completeLoadUserData = true
                                            }
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