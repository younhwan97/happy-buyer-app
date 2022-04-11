package kr.co.younhwan.happybuyer.adapter.product.contract

import kr.co.younhwan.happybuyer.data.ProductItem

interface ProductAdapterContract {

    interface View{

        var onClickFuncOfProduct: ((ProductItem) -> Unit)?
        var onClickFuncOfBasketBtn: ((Int, Int) -> Unit)?

        fun notifyAdapter()

        fun notifyAdapterByRange(start: Int, count: Int)
    }

    interface Model{

        fun addItems(productItems: ArrayList<ProductItem>)

        fun clearItem()

        fun getItem(position: Int): ProductItem

        fun getItems() : ArrayList<ProductItem>

        fun getItemCount(): Int

    }
}