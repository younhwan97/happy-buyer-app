package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource{

    interface LoadImageCallback {
        fun onLoadImages(list: ArrayList<ProductItem>)
    }

    fun getImages(context: Context, selectedCategory:String, loadImageCallback: LoadImageCallback?)
}