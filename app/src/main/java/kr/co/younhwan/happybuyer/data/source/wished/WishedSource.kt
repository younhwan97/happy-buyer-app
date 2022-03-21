package kr.co.younhwan.happybuyer.data.source.wished

interface WishedSource {
    fun createOrDeleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        createOrDeleteProductCallback: CreateOrDeleteProductCallback?
    )

    interface CreateOrDeleteProductCallback {
        fun onCreateOrDeleteProduct(perform: String?)
    }

    fun readProductsId(
        kakaoAccountId: Long,
        readProductsIdCallback: ReadProductsIdCallback?
    )

    interface ReadProductsIdCallback {
        fun onReadProductsId(list: ArrayList<Int>)
    }
}