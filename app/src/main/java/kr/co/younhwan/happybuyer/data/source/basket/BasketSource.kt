package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketSource {
    // CREATE
    fun createOrUpdateProduct(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createOrUpdateProductCallback: CreateOrUpdateProductCallback?
    )

    interface CreateOrUpdateProductCallback {
        fun onCreateOrUpdateProduct(resultCount: Int)
    }

    // READ
    fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: ReadProductsCallback?
    )

    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<BasketItem>)
    }

    // UPDATE
    fun updateProduct(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductCallback: UpdateProductCallback?
    )

    interface UpdateProductCallback {
        fun onUpdateProduct(isSuccess: Boolean)
    }

    // DELETE
    fun deleteProducts(
        kakaoAccountId: Long,
        productId: ArrayList<Int>,
        deleteProductsCallback: DeleteProductsCallback?
    )

    interface DeleteProductsCallback {
        fun onDeleteProducts(isSuccess: Boolean)
    }
}