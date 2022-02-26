package kr.co.younhwan.happybuyer.view.main.home.adapter.event.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface EventAdapterContract{
    interface View{

        fun notifyAdapter()

    }

    interface Model{
        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem
    }
}