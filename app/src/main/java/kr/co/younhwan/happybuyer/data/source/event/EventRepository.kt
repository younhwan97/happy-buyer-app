package kr.co.younhwan.happybuyer.data.source.event

import kr.co.younhwan.happybuyer.data.ProductItem

object EventRepository : EventSource {

    private val eventRemoteDataSource = EventRemoteDataSource

    // READ
    override fun readProducts(sortBy:String?, page:Int, readProductsCallback: EventSource.ReadProductsCallback?) {
        eventRemoteDataSource.readProducts(sortBy, page, object : EventSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }
}