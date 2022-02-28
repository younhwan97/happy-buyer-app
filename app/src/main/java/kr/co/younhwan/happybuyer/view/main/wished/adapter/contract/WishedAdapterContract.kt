package kr.co.younhwan.happybuyer.view.main.wished.adapter.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface WishedAdapterContract {
    interface View {

        var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)?

        var onClickFuncOfBasketBtn: ((Int) -> Unit)?

        fun notifyAdapter()
    }

    interface Model {

        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

        fun getItemCount(): Int

        fun deleteItem(position: Int)
    }
}