package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.BasketItem
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

    override fun readProducts(selectedCategory:String, sort:String, keyword:String?, readProductsCallback: ProductSource.ReadProductsCallback?) {
        productRemoteDataSource.readProducts(selectedCategory, sort, keyword, object : ProductSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }

    override fun readProduct(
        productId: Int,
        kakaoAccountId: Long,
        readProductCallback: ProductSource.ReadProductCallback?
    ) {
        productRemoteDataSource.readProduct(productId, kakaoAccountId, object : ProductSource.ReadProductCallback{
            override fun onReadProduct(productItem: ProductItem?) {
                readProductCallback?.onReadProduct(productItem)
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


}