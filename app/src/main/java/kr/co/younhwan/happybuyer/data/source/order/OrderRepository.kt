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
                override fun onCreate(isSuccess: Boolean) {
                    createCallback?.onCreate(isSuccess)
                }
            })
    }
}