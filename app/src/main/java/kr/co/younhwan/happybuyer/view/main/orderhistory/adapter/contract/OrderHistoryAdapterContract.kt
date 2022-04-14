package kr.co.younhwan.happybuyer.view.orderhistory.adapter.contract

import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderHistoryAdapterContract {
    interface View {

        fun notifyAdapter()

        fun notifyAdapterByRange(start: Int, count:Int)

        var onClickFun: ((OrderItem) -> Unit)?

        fun deleteLoading()

        fun notifyLastItemRemoved()

    }

    interface Model {

        fun addItems(orderHistoryItems: ArrayList<OrderItem>)

        fun clearItem()

        fun getItemCount(): Int

    }
}