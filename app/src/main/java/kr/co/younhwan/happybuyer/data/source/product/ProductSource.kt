package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource{

    // Create Product
    interface CreateProductInBasketCallback{
        fun onCreateProductInBasket(isSuccess: Boolean)
    }

    fun createProductInBasket(kakaoAccountId: Long, productId: Int, createProductInBasketCallback: CreateProductInBasketCallback?)

    interface CreateProductInWishedCallback{
        fun onCreateProductInWished(perform: String?)
    }

    fun createProductInWished(kakaoAccountId: Long, productId: Int, createProductInWishedCallback: CreateProductInWishedCallback?)

    // Read Product
    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<ProductItem>)
    }

    fun readProducts(selectedCategory:String, sort:String, readProductCallback: ReadProductsCallback?)

    interface ReadProductsInBasketCallback{
        fun onReadProductsInBasket(list: ArrayList<ProductItem>)
    }

    fun readProductsInBasket(kakaoAccountId: Long, readProductsInBasketCallback: ReadProductsInBasketCallback?)

    interface ReadProductsInBasketCountCallback{
        fun onReadProductsInBasketCount(count: Int)
    }

    fun readProductsInBasketCount(kakaoAccountId: Long, readProductsInBasketCountCallback: ReadProductsInBasketCountCallback?)

    interface ReadEventProductsCallback{
        fun onReadEventProduct(list: ArrayList<ProductItem>)
    }

    fun readEventProducts(readEventProductsCallback: ReadEventProductsCallback?)

    interface ReadWishedProductsIdCallback{
        fun onReadWishedProductsId(list: ArrayList<Int>)
    }

    fun readWishedProductsId(kakaoAccountId: Long, readWishedProductsIdCallback: ReadWishedProductsIdCallback?)

    // Update Product
    // ..

    // Delete Product
    // ..
}