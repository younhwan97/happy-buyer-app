package kr.co.younhwan.happybuyer.view.category.adapter.contract

import android.view.View
import kr.co.younhwan.happybuyer.data.ProductItem

interface CategoryAdapterContract{
    interface View{

        var onClickFuncOfWishedBtn: ((Int, Int) -> Unit)?

        var onClickFuncOfBasketBtn: ((Int) -> Unit)?

        fun notifyAdapter()

        fun notifyItem(position: Int)
    }

    interface Model{

        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

        fun updateProduct(position: Int, what: String)

    }

}