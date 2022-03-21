package kr.co.younhwan.happybuyer.data.source.event

import kr.co.younhwan.happybuyer.data.ProductItem

interface EventSource {
    // READ
    fun readProducts(readProductsCallback: ReadProductsCallback?)

    interface ReadProductsCallback {
        fun onReadProducts(list: ArrayList<ProductItem>)
    }
}