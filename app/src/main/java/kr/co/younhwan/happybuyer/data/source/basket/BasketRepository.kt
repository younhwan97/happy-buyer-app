package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

object BasketRepository : BasketSource {

    private val basketRemoteDataSource = BasketRemoteDataSource

    // CREATE
    override fun createOrUpdateProduct(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createOrUpdateProductCallback: BasketSource.CreateOrUpdateProductCallback?
    ) {
        basketRemoteDataSource.createOrUpdateProduct(
            kakaoAccountId,
            productId,
            count,
            object : BasketSource.CreateOrUpdateProductCallback {
                override fun onCreateOrUpdateProduct(resultCount: Int) {
                    createOrUpdateProductCallback?.onCreateOrUpdateProduct(resultCount)
                }
            })
    }

    // READ
    override fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: BasketSource.ReadProductsCallback?
    ) {
        basketRemoteDataSource.readProducts(
            kakaoAccountId,
            object : BasketSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<BasketItem>) {
                    readProductsCallback?.onReadProducts(list)
                }
            })
    }

    // UPDATE
    override fun updateProduct(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductCallback: BasketSource.UpdateProductCallback?
    ) {
        basketRemoteDataSource.updateProduct(
            kakaoAccountId,
            productId,
            perform,
            object : BasketSource.UpdateProductCallback {
                override fun onUpdateProduct(isSuccess: Boolean) {
                    updateProductCallback?.onUpdateProduct(isSuccess)
                }
            })
    }

    // DELETE
    override fun deleteProducts(
        kakaoAccountId: Long,
        productId: ArrayList<Int>,
        deleteProductsCallback: BasketSource.DeleteProductsCallback?
    ) {
        basketRemoteDataSource.deleteProducts(
            kakaoAccountId,
            productId,
            object : BasketSource.DeleteProductsCallback {
                override fun onDeleteProducts(isSuccess: Boolean) {
                    deleteProductsCallback?.onDeleteProducts(isSuccess)
                }
            })
    }
}