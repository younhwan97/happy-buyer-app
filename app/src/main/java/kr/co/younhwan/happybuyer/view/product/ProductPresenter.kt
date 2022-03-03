package kr.co.younhwan.happybuyer.view.product

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource

class ProductPresenter(
    private val view: ProductContract.View,
    private val productData: ProductRepository,
) : ProductContract.Model {

    override fun clickWishedBtn(productId: Int) {
        val app = view.getAct().application as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            productData.createProductInWished(
                app.kakaoAccountId,
                productId,
                object : ProductSource.CreateProductInWishedCallback {
                    override fun onCreateProductInWished(perform: String?) {
                        view.createProductInWishedResultCallback(productId, perform)
                    }
                }
            )
        }
    }
}