package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource



    override fun readProducts(selectedCategory:String, sort:String, keyword:String?, readProductsCallback: ProductSource.ReadProductsCallback?) {
        productRemoteDataSource.readProducts(selectedCategory, sort, keyword, object : ProductSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }

    override fun readProduct(
        productId: Int,
        kakaoAccountId: Long,
        readProductCallback: ProductSource.ReadProductCallback?
    ) {
        productRemoteDataSource.readProduct(productId, kakaoAccountId, object : ProductSource.ReadProductCallback{
            override fun onReadProduct(productItem: ProductItem?) {
                readProductCallback?.onReadProduct(productItem)
            }
        })
    }
}