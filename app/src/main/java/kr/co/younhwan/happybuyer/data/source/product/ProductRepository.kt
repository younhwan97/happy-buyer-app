package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource{

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun createProductInWished(kakaoAccountId: Long, productId: Int, createProductInWishedCallback: ProductSource.CreateProductInWishedCallback?) {
        productRemoteDataSource.createProductInWished(kakaoAccountId, productId, object :ProductSource.CreateProductInWishedCallback{
            override fun onCreateProductInWished(explain: String?) {
                createProductInWishedCallback?.onCreateProductInWished(explain)
            }
        })
    }

    override fun createProductInBasket(kakaoAccountId: Long, productId: Int, createProductInBasketCallback: ProductSource.CreateProductInBasketCallback?) {
        productRemoteDataSource.createProductInBasket(kakaoAccountId, productId, object : ProductSource.CreateProductInBasketCallback{
            override fun onCreateProductInBasket(isSuccess: Boolean) {
                createProductInBasketCallback?.onCreateProductInBasket(isSuccess)
            }
        })
    }


    override fun readProducts(kakaoAccountId: Long?, selectedCategory:String, readProductCallback: ProductSource.ReadProductsCallback?) {
        productRemoteDataSource.readProducts(kakaoAccountId, selectedCategory, object : ProductSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductCallback?.onReadProducts(list)
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

    override fun readProductsInBasketCount(kakaoAccountId: Long, readProductsInBasketCountCallback: ProductSource.ReadProductsInBasketCountCallback?) {
        productRemoteDataSource.readProductsInBasketCount(kakaoAccountId, object : ProductSource.ReadProductsInBasketCountCallback{
            override fun onReadProductsInBasketCount(count: Int) {
                readProductsInBasketCountCallback?.onReadProductsInBasketCount(count)
            }
        })
    }
}