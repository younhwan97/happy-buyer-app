package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun createProductInWished(kakaoAccountId: Long, productId: Int, createProductInWishedCallback: ProductSource.CreateProductInWishedCallback?) {
        productRemoteDataSource.createProductInWished(kakaoAccountId, productId, object :ProductSource.CreateProductInWishedCallback{
            override fun onCreateProductInWished(perform: String?) {
                createProductInWishedCallback?.onCreateProductInWished(perform)
            }
        })
    }




    override fun readProducts(selectedCategory:String, sort:String, readProductCallback: ProductSource.ReadProductsCallback?) {
        productRemoteDataSource.readProducts(selectedCategory, sort, object : ProductSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductCallback?.onReadProducts(list)
            }
        })
    }



    override fun readEventProducts(readEventProductsCallback: ProductSource.ReadEventProductsCallback?) {
        productRemoteDataSource.readEventProducts(object : ProductSource.ReadEventProductsCallback{
            override fun onReadEventProduct(list: ArrayList<ProductItem>) {
                readEventProductsCallback?.onReadEventProduct(list)
            }
        })
    }

    override fun readWishedProductsId(kakaoAccountId: Long, readWishedProductsIdCallback: ProductSource.ReadWishedProductsIdCallback?) {
        productRemoteDataSource.readWishedProductsId(kakaoAccountId, object : ProductSource.ReadWishedProductsIdCallback{
            override fun onReadWishedProductsId(list: ArrayList<Int>) {
                readWishedProductsIdCallback?.onReadWishedProductsId(list)
            }
        })
    }




    /***********************************************************************/
    /******************************* Basket *******************************/

    override fun createProductInBasket(kakaoAccountId: Long, productId: Int, createProductInBasketCallback: ProductSource.CreateProductInBasketCallback?) {
        productRemoteDataSource.createProductInBasket(kakaoAccountId, productId, object : ProductSource.CreateProductInBasketCallback{
            override fun onCreateProductInBasket(count: Int) {
                createProductInBasketCallback?.onCreateProductInBasket(count)
            }
        })
    }

    override fun readProductsInBasket(kakaoAccountId: Long, readProductsInBasketCallback: ProductSource.ReadProductsInBasketCallback?) {
        productRemoteDataSource.readProductsInBasket(kakaoAccountId, object : ProductSource.ReadProductsInBasketCallback {
            override fun onReadProductsInBasket(list: ArrayList<ProductItem>) {
                readProductsInBasketCallback?.onReadProductsInBasket(list)
            }
        })
    }

    override fun minusProductInBasket(kakaoAccountId: Long, productId: Int, minusProductInBasketCallback: ProductSource.MinusProductInBasketCallback?) {
        productRemoteDataSource.minusProductInBasket(kakaoAccountId, productId, object : ProductSource.MinusProductInBasketCallback{
            override fun onMinusProductInBasket(isSuccess: Boolean) {
                minusProductInBasketCallback?.onMinusProductInBasket(isSuccess)
            }
        })
    }

    override fun deleteProductInBasket(kakaoAccountId: Long, productId: Int, deleteProductInBasketCallback: ProductSource.DeleteProductInBasketCallback?) {
        productRemoteDataSource.deleteProductInBasket(kakaoAccountId, productId, object : ProductSource.DeleteProductInBasketCallback{
            override fun onDeleteProductInBasket(isSuccess: Boolean) {
                deleteProductInBasketCallback?.onDeleteProductInBasket(isSuccess)
            }
        })
    }
}