package kr.co.younhwan.happybuyer.view.product

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedSource

class ProductPresenter(
    private val view: ProductContract.View,
    private val productData: ProductRepository,
    private val wishedData: WishedRepository,
) : ProductContract.Model {

    override fun clickWishedBtn(productId: Int) {
        val app = view.getAct().application as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            wishedData.createOrDeleteProduct(
                app.kakaoAccountId,
                productId,
                object : WishedSource.CreateOrDeleteProductCallback {
                    override fun onCreateOrDeleteProduct(perform: String?) {
                        view.createProductInWishedResultCallback(productId, perform)
                    }
                }
            )
        }
    }

    override fun createProductInBasket(kakaoAccountId: Long, productId: Int, count: Int) {
//        productData.createProductInBasket(
//            kakaoAccountId,
//            productId,
//            count,
//            object : ProductSource.CreateProductInBasketCallback{
//                override fun onCreateProductInBasket(count: Int) {
//                    view.createProductInBasketResultCallback(count)
//                }
//            }
//        )
    }
}