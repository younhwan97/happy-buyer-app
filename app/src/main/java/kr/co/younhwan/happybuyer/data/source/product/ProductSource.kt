package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource{

    interface LoadProductCallback {
        fun onLoadProducts(list: ArrayList<ProductItem>)
    }

    interface LoadWishedProductCallback{
        fun onLoadWishedProducts(list: ArrayList<ProductItem>)
    }

    interface AddProductToBasketCallback{
        fun onAddProductToBasket(success: Boolean)
    }

    interface AddProductToWishedCallback{
        fun onAddProductToWished(explain: String?)
    }

    interface DeleteProductInWishedCallback{
        fun onDeleteProductInWished()
    }

    fun getProducts(context: Context, selectedCategory:String, kakaoAccountId: Long?, loadImageCallback: LoadProductCallback?)

    fun getWishedProducts(context: Context, kakaoAccountId: Long?, loadWishedProductCallback: LoadWishedProductCallback?)

    fun addProductToBasket(kakaoAccountId: Long, productId: Int, addProductCallback: AddProductToBasketCallback?)

    fun addProductToWished(kakaoAccountId: Long, productId: Int, addProductToWishedCallback: AddProductToWishedCallback?)

    fun deleteProductInWished(kakaoAccountId: Long, productId: Int, deleteProductInWishedCallback: DeleteProductInWishedCallback?)
}