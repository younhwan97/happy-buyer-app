package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource {

    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<ProductItem>)
    }

    fun readProducts(
        selectedCategory: String,
        sort: String,
        keyword: String?,
        readProductsCallback: ReadProductsCallback?
    )

    interface ReadProductCallback{
        fun onReadProduct(productItem: ProductItem?)
    }

    fun readProduct(
        productId: Int,
        kakaoAccountId: Long,
        readProductCallback: ReadProductCallback?
    )
}