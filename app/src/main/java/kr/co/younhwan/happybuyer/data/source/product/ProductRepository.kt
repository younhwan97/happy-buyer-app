package kr.co.younhwan.happybuyer.data.source.product

import kr.co.younhwan.happybuyer.data.ProductItem

object ProductRepository : ProductSource {

    private val productRemoteDataSource = ProductRemoteDataSource

    override fun readProducts(
        selectedCategory: String,
        sortBy:String?,
        page:Int,
        keyword: String?,
        readProductsCallback: ProductSource.ReadProductsCallback?
    ) {
        productRemoteDataSource.readProducts(
            selectedCategory,
            sortBy,
            page,
            keyword,
            object : ProductSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    readProductsCallback?.onReadProducts(list)
                }
            })
    }
}