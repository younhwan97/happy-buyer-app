package kr.co.younhwan.happybuyer.data.source.order

import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderSource {

    fun create(
        kakaoAccountId: Long,
        orderItem: OrderItem,
        createCallback: CreateCallback?
    )

    interface CreateCallback {
        fun onCreate(isSuccess: Boolean)
    }
}