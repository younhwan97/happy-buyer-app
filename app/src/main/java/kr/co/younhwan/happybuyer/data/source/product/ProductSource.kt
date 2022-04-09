package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductSource {

    fun readProducts(
        selectedCategory: String,
        page: Int,
        keyword: String?,
        readProductsCallback: ReadProductsCallback?
    )

    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<ProductItem>)
    }
}