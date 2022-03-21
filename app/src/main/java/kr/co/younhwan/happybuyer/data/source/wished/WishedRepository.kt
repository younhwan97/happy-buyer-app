package kr.co.younhwan.happybuyer.data.source.wished

import kr.co.younhwan.happybuyer.data.ProductItem

object WishedRepository : WishedSource {

    private val wishedRemoteDataSource = WishedRemoteDataSource

    // CREATE or DELETE
    override fun createOrDeleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        createOrDeleteProductCallback: WishedSource.CreateOrDeleteProductCallback?
    ) {
        wishedRemoteDataSource.createOrDeleteProduct(kakaoAccountId, productId, object :
            WishedSource.CreateOrDeleteProductCallback {
            override fun onCreateOrDeleteProduct(perform: String?) {
                createOrDeleteProductCallback?.onCreateOrDeleteProduct(perform)
            }
        })
    }

    // READ
    override fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: WishedSource.ReadProductsCallback?
    ) {
        wishedRemoteDataSource.readProducts(kakaoAccountId, object :
            WishedSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }
}