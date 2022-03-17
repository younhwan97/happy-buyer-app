package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketSource {
    // CREATE
    fun createProduct(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductCallback: CreateProductCallback?
    )

    interface CreateProductCallback {
        fun onCreateProduct(resultCount: Int)
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
    fun deleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductCallback: DeleteProductCallback?
    )

    interface DeleteProductCallback {
        fun onDeleteProduct(isSuccess: Boolean)
    }

//    interface DeleteProductsCallback{
//        fun onDeleteProducts(isSuccess: Boolean)
//    }
//
//    fun deleteProducts(
//
//    )
}