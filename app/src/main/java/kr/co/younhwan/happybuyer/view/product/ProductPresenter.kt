package kr.co.younhwan.happybuyer.view.product

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedSource

class ProductPresenter(
    private val view: ProductContract.View,
    private val basketData: BasketRepository,
    private val wishedData: WishedRepository,
) : ProductContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun clickWishedBtn(productId: Int) {
        if (app.isLogined) {
            // 로그인 상태일 때
            wishedData.createOrDeleteProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                createOrDeleteProductCallback = object : WishedSource.CreateOrDeleteProductCallback {
                    override fun onCreateOrDeleteProduct(perform: String?) {
                        view.createOrDeleteProductInWishedCallback(productId, perform)
                    }
                }
            )
        } else {
            // 로그인 상태가 아닐 때
            view.createLoginActivity()
        }
    }

    override fun createOrUpdateProductInBasket(productId: Int, count: Int) {
        if(app.isLogined){
            // 로그인 상태일 때
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = count,
                createOrUpdateProductCallback = object : BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {
                        // 장바구니에 상품을 추가 하고, 장바구니에 담긴 상품 종류의 개수를 읽어온다.
                        basketData.readProducts(
                            kakaoAccountId = app.kakaoAccountId,
                            readProductsCallback = object : BasketSource.ReadProductsCallback {
                                override fun onReadProducts(list: ArrayList<BasketItem>) {
                                    view.createOrUpdateProductInBasketCallback(
                                        resultCount = resultCount,
                                        basketItemCount = list.size
                                    )
                                }
                            }
                        )
                    }
                }
            )
        } else {
            // 로그인 상태가 아닐 때
            view.createLoginActivity()
        }
    }
}