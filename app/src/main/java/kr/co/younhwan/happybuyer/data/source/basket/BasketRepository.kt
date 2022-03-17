package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

object BasketRepository : BasketSource {

    private val basketRemoteDataSource = BasketRemoteDataSource

    override fun createProduct(kakaoAccountId: Long, productId: Int, count:Int, createProductCallback: BasketSource.CreateProductCallback?) {
        basketRemoteDataSource.createProduct(kakaoAccountId, productId, count, object : BasketSource.CreateProductCallback{
            override fun onCreateProduct(resultCount: Int) {
                createProductCallback?.onCreateProduct(resultCount)
            }
        })
    }

    override fun readProducts(kakaoAccountId: Long, readProductsCallback: BasketSource.ReadProductsCallback?) {
        basketRemoteDataSource.readProducts(kakaoAccountId, object : BasketSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<BasketItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }

    override fun updateProduct(kakaoAccountId: Long, productId: Int, perform:String, updateProductCallback: BasketSource.UpdateProductCallback?) {
        basketRemoteDataSource.updateProduct(kakaoAccountId, productId, perform, object : BasketSource.UpdateProductCallback{
            override fun onUpdateProduct(isSuccess: Boolean) {
                updateProductCallback?.onUpdateProduct(isSuccess)
            }
        })
    }

    override fun deleteProduct(kakaoAccountId: Long, productId: Int, deleteProductCallback: BasketSource.DeleteProductCallback?) {
        basketRemoteDataSource.deleteProduct(kakaoAccountId, productId, object : BasketSource.DeleteProductCallback{
            override fun onDeleteProduct(isSuccess: Boolean) {
                deleteProductCallback?.onDeleteProduct(isSuccess)
            }
        })
    }
}