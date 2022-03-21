package kr.co.younhwan.happybuyer.data.source.wished

object WishedRepository : WishedSource{

    private val wishedRemoteDataSource = WishedRemoteDataSource

    override fun createOrDeleteProduct(kakaoAccountId: Long, productId: Int, createOrDeleteProductCallback: WishedSource.CreateOrDeleteProductCallback?) {
        wishedRemoteDataSource.createOrDeleteProduct(kakaoAccountId, productId, object :
            WishedSource.CreateOrDeleteProductCallback{
            override fun onCreateOrDeleteProduct(perform: String?) {
                createOrDeleteProductCallback?.onCreateOrDeleteProduct(perform)
            }
        })
    }


    override fun readProductsId(kakaoAccountId: Long, readProductsIdCallback: WishedSource.ReadProductsIdCallback?) {
        wishedRemoteDataSource.readProductsId(kakaoAccountId, object : WishedSource.ReadProductsIdCallback{
            override fun onReadProductsId(list: ArrayList<Int>) {
                readProductsIdCallback?.onReadProductsId(list)
            }
        })
    }
}