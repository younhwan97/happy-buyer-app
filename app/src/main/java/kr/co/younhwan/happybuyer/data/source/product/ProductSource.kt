package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

interface ProductSource{

    interface LoadImageCallback {
        fun onLoadImages(list: ArrayList<CategoryItem>)
    }

    fun getImages(context: Context, selectedCategory:String, loadImageCallback: LoadImageCallback?)
}