package kr.co.younhwan.happybuyer.data.source.event

import kr.co.younhwan.happybuyer.data.ProductItem

interface EventSource {

    fun readEventProducts(readEventProductsCallback: ReadEventProductsCallback?)

    interface ReadEventProductsCallback {
        fun onReadEventProducts(list: ArrayList<ProductItem>)
    }

}