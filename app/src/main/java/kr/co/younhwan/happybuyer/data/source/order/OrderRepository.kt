package kr.co.younhwan.happybuyer.data.source.order

import kr.co.younhwan.happybuyer.data.OrderItem


object OrderRepository : OrderSource {

    private val orderRemoteDataSource = OrderRemoteDataSource

    override fun create(
        kakaoAccountId: Long,
        orderItem: OrderItem,
        createCallback: OrderSource.CreateCallback?
    ) {
        orderRemoteDataSource.create(
            kakaoAccountId,
            orderItem,
            object : OrderSource.CreateCallback {
                override fun onCreate(orderId: Int) {
                    createCallback?.onCreate(orderId)
                }
            })
    }

    override fun read(kakaoAccountId: Long, readCallback: OrderSource.ReadCallback?) {
        orderRemoteDataSource.read(
            kakaoAccountId,
            object : OrderSource.ReadCallback {
                override fun onRead(list: ArrayList<OrderItem>) {
                    readCallback?.onRead(list)
                }
            }
        )
    }
}