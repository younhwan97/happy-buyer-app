package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun getProducts(context: Context, selectedCategory:String, kakaoAccountId: Long?, loadImageCallback: ProductSource.LoadProductCallback?) {
        productRemoteDataSource.getProducts(context, selectedCategory, kakaoAccountId, object : ProductSource.LoadProductCallback {
            override fun onLoadProducts(list: ArrayList<ProductItem>) {
                loadImageCallback?.onLoadProducts(list)
            }
        })
    }


    override fun getBasketProducts(context: Context, kakaoAccountId: Long, loadBasketProductCallback: ProductSource.LoadBasketProductCallback?) {
        productRemoteDataSource.getBasketProducts(context, kakaoAccountId, object : ProductSource.LoadBasketProductCallback {
            override fun onLoadBasketProducts(list: ArrayList<ProductItem>) {
                loadBasketProductCallback?.onLoadBasketProducts(list)
            }
        })
    }

    override fun addProductToBasket(kakaoAccountId: Long, productId: Int, addProductCallback: ProductSource.AddProductToBasketCallback?) {
        productRemoteDataSource.addProductToBasket(kakaoAccountId, productId, object : ProductSource.AddProductToBasketCallback{
            override fun onAddProductToBasket(success: Boolean) {
                addProductCallback?.onAddProductToBasket(success)
            }
        })
    }

    override fun addProductToWished(kakaoAccountId: Long, productId: Int, addProductToWishedCallback: ProductSource.AddProductToWishedCallback?) {
        productRemoteDataSource.addProductToWished(kakaoAccountId, productId, object :ProductSource.AddProductToWishedCallback{
            override fun onAddProductToWished(explain: String?) {
                addProductToWishedCallback?.onAddProductToWished(explain)
            }
        })
    }
}