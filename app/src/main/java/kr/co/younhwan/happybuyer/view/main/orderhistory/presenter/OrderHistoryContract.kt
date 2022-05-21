package kr.co.younhwan.happybuyer.view.main.orderhistory.presenter

import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface OrderHistoryContract {
    interface View {

        fun getAct(): MainActivity

        fun loadOrderHistoryCallback(resultCount: Int)

        fun createOrderDetailAct(orderHistoryItem: OrderItem)

    }

    interface Model {

        fun loadOrderHistory(isClear: Boolean, page: Int)

        fun loadMoreOrderHistory(page: Int)

    }
}