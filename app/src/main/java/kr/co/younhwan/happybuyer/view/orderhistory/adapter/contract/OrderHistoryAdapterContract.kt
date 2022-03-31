package kr.co.younhwan.happybuyer.view.orderhistory.adapter.contract

import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderHistoryAdapterContract {
    interface View {
        fun notifyAdapter()

        var onClickFun: ((Int) -> Unit)?
    }

    interface Model {
        fun addItems(orderHistoryItems: ArrayList<OrderItem>)
    }
}