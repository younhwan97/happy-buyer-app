package kr.co.younhwan.happybuyer.data.source.basket

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketSource {
    interface CreateProductInBasketCallback {
        fun onCreateProductInBasket(resultCount: Int)
    }

    fun createProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductInBasketCallback: CreateProductInBasketCallback?
    )

    interface ReadProductsInBasketCallback {
        fun onReadProductsInBasket(list: ArrayList<BasketItem>)
    }

    fun readProductsInBasket(
        kakaoAccountId: Long,
        readProductsInBasketCallback: ReadProductsInBasketCallback?
    )

    interface UpdateProductInBasketCallback {
        fun onUpdateProductInBasket(isSuccess: Boolean)
    }

    fun updateProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductInBasketCallback: UpdateProductInBasketCallback?
    )


    interface DeleteProductInBasketCallback {
        fun onDeleteProductInBasket(isSuccess: Boolean)
    }

    fun deleteProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductInBasketCallback: DeleteProductInBasketCallback?
    )

}