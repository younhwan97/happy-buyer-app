package kr.co.younhwan.happybuyer.view.basket

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketContract{
    interface View{

        fun getAct(): BasketActivity

        fun calculatePriceCallback(totalPrice: Int, originalTotalPrice: Int, basketItemCount: Int)

        fun onClickCheckBoxCallback(isCheckedAllBasketItem: Boolean)
    }

    interface Model{

        fun loadBasketProduct(isClear: Boolean)

        fun checkAllBasketProduct(newStatus: Boolean)

        fun calculatePrice()

        fun deleteSelectedItems()
    }
}