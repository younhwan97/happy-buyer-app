package kr.co.younhwan.happybuyer.data.source.wished

import kr.co.younhwan.happybuyer.data.ProductItem

interface WishedSource {
    // CREATE or DELETE
    fun createOrDeleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        createOrDeleteProductCallback: CreateOrDeleteProductCallback?
    )

    interface CreateOrDeleteProductCallback {
        fun onCreateOrDeleteProduct(perform: String?)
    }

    // READ
    fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: ReadProductsCallback?
    )

    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<ProductItem>)
    }
}