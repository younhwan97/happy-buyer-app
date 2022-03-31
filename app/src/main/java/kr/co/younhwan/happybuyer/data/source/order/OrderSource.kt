package kr.co.younhwan.happybuyer.data.source.order

import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderSource {

    fun create(
        kakaoAccountId: Long,
        orderItem: OrderItem,
        createCallback: CreateCallback?
    )

    interface CreateCallback {
        fun onCreate(orderId: Int)
    }

    fun read(
        kakaoAccountId: Long,
        readCallback: ReadCallback?
    )

    interface ReadCallback {
        fun onRead(list: ArrayList<OrderItem>)
    }
}