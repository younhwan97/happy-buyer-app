package kr.co.younhwan.happybuyer.data.source.event

import kr.co.younhwan.happybuyer.data.ProductItem

object EventRepository : EventSource {

    private val eventRemoteDataSource = EventRemoteDataSource

    override fun readProducts(readProductsCallback: EventSource.ReadProductsCallback?) {
        eventRemoteDataSource.readProducts(object : EventSource.ReadProductsCallback {
            override fun onReadProducts(list: ArrayList<ProductItem>) {
                readProductsCallback?.onReadProducts(list)
            }
        })
    }
}