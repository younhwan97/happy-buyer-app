package kr.co.younhwan.happybuyer.view.orderhistory

import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderHistoryContract {
    interface View {

        fun getView(): OrderHistoryActivity

        fun loadOrderHistoryCallback(resultCount: Int)

        fun createOrderDetailAct(orderHistoryItem: OrderItem)

    }

    interface Model {

        fun loadOrderHistory()

    }
}