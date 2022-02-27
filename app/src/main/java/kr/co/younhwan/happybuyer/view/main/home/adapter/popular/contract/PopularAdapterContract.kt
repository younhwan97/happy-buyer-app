package kr.co.younhwan.happybuyer.view.main.home.adapter.popular.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface PopularAdapterContract{
    interface View{
        fun notifyAdapter()
    }

    interface Model{
        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem
    }

}