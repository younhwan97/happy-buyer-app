package kr.co.younhwan.happybuyer.adapter.orderproduct.contract

import kr.co.younhwan.happybuyer.data.BasketItem

interface OrderAdapterContract {
    interface View {
        fun notifyAdapter()

    }

    interface Model {
        fun addItems(orderItems: ArrayList<BasketItem>)

        fun getItemCount(): Int

        fun getItem(position: Int): BasketItem
    }
}