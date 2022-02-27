package kr.co.younhwan.happybuyer.view.main.home.adapter.event.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface EventAdapterContract{
    interface View{

        var onClickFuncOfWishedBtn: ((Int, Int) -> Unit)?

        fun notifyAdapter()

        fun notifyItem(position: Int)

        fun notifyItemByUsingPayload(position: Int, payload: String)
    }

    interface Model{
        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

        fun getItemCount(): Int

        fun updateProduct(position: Int, productItem: ProductItem)
    }
}