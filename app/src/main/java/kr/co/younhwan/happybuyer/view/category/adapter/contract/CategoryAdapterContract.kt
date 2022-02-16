package kr.co.younhwan.happybuyer.view.category.adapter.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface CategoryAdapterContract{
    interface View{

        var onClickFuncHeartBtn: ((Int) -> Unit)?

        var onClickFuncShoppingCartBtn: ((Int) -> Unit)?

        fun notifyAdapter()
    }

    interface Model{

        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

    }

}