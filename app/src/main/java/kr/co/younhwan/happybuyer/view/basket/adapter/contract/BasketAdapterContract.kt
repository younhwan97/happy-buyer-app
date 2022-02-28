package kr.co.younhwan.happybuyer.view.basket.adapter.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface BasketAdapterContract{
    interface View{

        var onClickFuncOfPlusBtn: ((Int, Int) -> Unit)?

        var onClickFuncOfMinusBtn: ((Int, Int) -> Unit)?

        var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)?

        fun notifyAdapter()

        fun notifyItem(position: Int)

        fun notifyItemByUsingPayload(position: Int, payload: String)

    }

    interface Model{
        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

        fun deleteItem(position: Int)
    }
}