package kr.co.younhwan.happybuyer.data.source.event

import kr.co.younhwan.happybuyer.data.ProductItem

object EventRepository : EventSource {

    private val eventRemoteDataSource = EventRemoteDataSource

    override fun readEventProducts(readEventProductsCallback: EventSource.ReadEventProductsCallback?) {
        eventRemoteDataSource.readEventProducts(object : EventSource.ReadEventProductsCallback {
            override fun onReadEventProducts(list: ArrayList<ProductItem>) {
                readEventProductsCallback?.onReadEventProducts(list)
            }
        })
    }
}