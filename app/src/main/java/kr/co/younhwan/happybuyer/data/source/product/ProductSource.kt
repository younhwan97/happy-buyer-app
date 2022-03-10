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

    /***********************************************************************/
    /******************************* Event *******************************/
    interface ReadEventProductsCallback {
        fun onReadEventProduct(list: ArrayList<ProductItem>)
    }

    fun readEventProducts(readEventProductsCallback: ReadEventProductsCallback?)
    /***********************************************************************/
    /***********************************************************************/


    /***********************************************************************/
    /******************************* Wished *******************************/

    interface ReadWishedProductsIdCallback {
        fun onReadWishedProductsId(list: ArrayList<Int>)
    }

    fun readWishedProductsId(
        kakaoAccountId: Long,
        readWishedProductsIdCallback: ReadWishedProductsIdCallback?
    )

    interface CreateProductInWishedCallback {
        fun onCreateProductInWished(perform: String?)
    }

    fun createProductInWished(
        kakaoAccountId: Long,
        productId: Int,
        createProductInWishedCallback: CreateProductInWishedCallback?
    )

    /***********************************************************************/
    /***********************************************************************/


    /***********************************************************************/
    /******************************* Basket *******************************/

    interface CreateProductInBasketCallback { // CREATE
        fun onCreateProductInBasket(count: Int)
    }

    fun createProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductInBasketCallback: CreateProductInBasketCallback?
    )

    interface ReadProductsInBasketCallback { // READ
        fun onReadProductsInBasket(list: ArrayList<ProductItem>)
    }

    fun readProductsInBasket(
        kakaoAccountId: Long,
        readProductsInBasketCallback: ReadProductsInBasketCallback?
    )

    interface MinusProductInBasketCallback { // UPDATE (Minus)
        fun onMinusProductInBasket(isSuccess: Boolean)
    }

    fun minusProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        minusProductInBasketCallback: MinusProductInBasketCallback?
    )


    interface DeleteProductInBasketCallback { // DELETE
        fun onDeleteProductInBasket(isSuccess: Boolean)
    }

    fun deleteProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductInBasketCallback: DeleteProductInBasketCallback?
    )

    /***********************************************************************/
    /***********************************************************************/
}