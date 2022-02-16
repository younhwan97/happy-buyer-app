package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun getImages(context: Context, selectedCategory:String, loadImageCallback: ProductSource.LoadImageCallback?) {
        productRemoteDataSource.getImages(context, selectedCategory, object : ProductSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<ProductItem>) {
                loadImageCallback?.onLoadImages(list)
            }
        })
    }

    override fun addProductToBasket(productId: Int) {
        TODO("Not yet implemented")
    }

    override fun addProductToWished(productId: Int) {
        TODO("Not yet implemented")
    }
}