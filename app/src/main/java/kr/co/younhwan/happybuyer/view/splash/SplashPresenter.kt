package kr.co.younhwan.happybuyer.view.splash

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
    private val view : SplashContract.View,
    private val userData: UserRepository,
    private val productData: ProductRepository,
    private val wishedData: WishedRepository
) : SplashContract.Model {

    override fun loadUserInfo() {
        val app = ((view.getAct()).application) as GlobalApplication // 유저 정보를 저장할 application

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 (= 로그인 정보가 없을 때)

                app.isLogined = false
                view.finishSplashActivity()

            } else if (tokenInfo != null) { // 토큰이 있을 때 (= 로그인 정보가 있을 때)

                app.isLogined = true
                app.kakaoAccountId = tokenInfo.id!!

                // oAuth key(kakaoAccountId) 를 이용해 사용자 정보를 DB 에서 가져온다.
                userData.readUser(
                    app.kakaoAccountId!!,
                    object : UserSource.ReadUserCallback {
                        override fun onReadUser(userItem: UserItem?) {
                            // Application 에 사용자 정보를 업데이트한다.
                            app.nickname = userItem?.nickname // 유저 이름, 닉네임
                            app.pointNumber = userItem?.pointNumber // 유저 포인트 번호
                            app.shippingAddress = userItem?.shippingAddress // 유저 주소
                            app.activatedBasket =
                                userItem?.activatedBasket // 유저 장바구니 활성화 여부 (activate, deactivate, null)

                            wishedData.readProducts(
                                app.kakaoAccountId,
                                object : WishedSource.ReadProductsCallback {
                                    override fun onReadProducts(list: ArrayList<ProductItem>) {

                                        val productIdList = ArrayList<Int>()
                                        for(item in list){
                                            productIdList.add(item.productId)
                                        }


                                        app.wishedProductId = productIdList

                                        if(app.activatedBasket == "activate"){
//                                            productData.readProductsInBasketCount(
//                                                app.kakaoAccountId!!,
//                                                object : ProductSource.ReadProductsInBasketCountCallback {
//                                                    override fun onReadProductsInBasketCount(count: Int) {
//                                                        app.basketItemCount = count
//
//                                                        view.finishSplashActivity()
//                                                    }
//                                                }
//                                            )

                                            view.finishSplashActivity()
                                        } else { // deactivate or null
                                            view.finishSplashActivity()
                                        }
                                    }
                                }
                            )
                        }
                    })
            }
        }
    }

}