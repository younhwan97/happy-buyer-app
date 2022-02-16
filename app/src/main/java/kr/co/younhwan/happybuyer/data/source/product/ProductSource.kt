package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource{

    interface LoadImageCallback {
        fun onLoadImages(list: ArrayList<ProductItem>)
    }

    interface addProductCallback{
        fun onAddProduct()
    }

    fun getImages(context: Context, selectedCategory:String, loadImageCallback: LoadImageCallback?)

    fun addProductToWished(productId: Int)

    fun addProductToBasket(productId: Int)
}