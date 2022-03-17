package kr.co.younhwan.happybuyer.view.basket.adapter.contract

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketAdapterContract{
    interface View{

        var onClickFunOfCheckBox: ((Int, Boolean) -> Unit)?

        var onClickFunOfPlusBtn: ((BasketItem, Int) -> Unit)?

        var onClickFunOfMinusBtn: ((BasketItem, Int) -> Unit)?

        var onClickFunOfDeleteBtn: ((BasketItem, Int) -> Unit)?

        fun notifyAdapter()

        fun notifyItem(position: Int)

        fun notifyItemByUsingPayload(position: Int, payload: String)

    }

    interface Model{
        fun addItems(productItems: ArrayList<BasketItem>)

        fun clearItem()

        fun getItem(position: Int): BasketItem

        fun getItemCount(): Int

        fun deleteItem(position: Int)

        fun updateItem(position: Int, basketItem: BasketItem)

        fun updateItemCount(position: Int, count: Int)
    }
}