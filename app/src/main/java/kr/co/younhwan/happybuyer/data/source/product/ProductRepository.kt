package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.data.CategoryItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun getImages(context: Context, selectedCategory:String, loadImageCallback: ProductSource.LoadImageCallback?) {
        productRemoteDataSource.getImages(context, selectedCategory, object : ProductSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<CategoryItem>) {
                loadImageCallback?.onLoadImages(list)
            }
        })
    }
}