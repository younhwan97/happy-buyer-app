package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource{

    interface LoadProductCallback {
        fun onLoadProducts(list: ArrayList<ProductItem>)
    }

    interface AddProductToBasketCallback{
        fun onAddProductToBasket(success: Boolean)
    }

    interface AddProductToWishedCallback{
        fun onAddProductToWished(explain: String?)
    }

    fun getProducts(context: Context, selectedCategory:String, kakaoAccountId: Long?, loadImageCallback: LoadProductCallback?)

    fun addProductToBasket(kakaoAccountId: Long, productId: Int, addProductCallback: AddProductToBasketCallback?)

    fun addProductToWished(kakaoAccountId: Long, productId: Int, addProductToWishedCallback: AddProductToWishedCallback?)
}