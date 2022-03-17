package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

object BasketRepository : BasketSource {

    private val basketRemoteDataSource = BasketRemoteDataSource

    override fun createProductInBasket(kakaoAccountId: Long, productId: Int, count:Int, createProductInBasketCallback: BasketSource.CreateProductInBasketCallback?) {
        basketRemoteDataSource.createProductInBasket(kakaoAccountId, productId, count, object : BasketSource.CreateProductInBasketCallback{
            override fun onCreateProductInBasket(resultCount: Int) {
                createProductInBasketCallback?.onCreateProductInBasket(resultCount)
            }
        })
    }

    override fun readProductsInBasket(kakaoAccountId: Long, readProductsInBasketCallback: BasketSource.ReadProductsInBasketCallback?) {
        basketRemoteDataSource.readProductsInBasket(kakaoAccountId, object : BasketSource.ReadProductsInBasketCallback {
            override fun onReadProductsInBasket(list: ArrayList<BasketItem>) {
                readProductsInBasketCallback?.onReadProductsInBasket(list)
            }
        })
    }

    override fun updateProductInBasket(kakaoAccountId: Long, productId: Int, perform:String, updateProductInBasketCallback: BasketSource.UpdateProductInBasketCallback?) {
        basketRemoteDataSource.updateProductInBasket(kakaoAccountId, productId, perform, object : BasketSource.UpdateProductInBasketCallback{
            override fun onUpdateProductInBasket(isSuccess: Boolean) {
                updateProductInBasketCallback?.onUpdateProductInBasket(isSuccess)
            }
        })
    }

    override fun deleteProductInBasket(kakaoAccountId: Long, productId: Int, deleteProductInBasketCallback: BasketSource.DeleteProductInBasketCallback?) {
        basketRemoteDataSource.deleteProductInBasket(kakaoAccountId, productId, object : BasketSource.DeleteProductInBasketCallback{
            override fun onDeleteProductInBasket(isSuccess: Boolean) {
                deleteProductInBasketCallback?.onDeleteProductInBasket(isSuccess)
            }
        })
    }
}