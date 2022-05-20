package kr.co.younhwan.happybuyer.view.basket

import kr.co.younhwan.happybuyer.data.BasketItem

interface BasketContract{
    interface View{

        fun getAct(): BasketActivity

        fun loadBasketProductsCallback(resultCount: Int)

        fun calculatePriceCallback(totalPrice: Int, originalTotalPrice: Int, basketItemCount: Int)

        fun onClickCheckBoxCallback(isCheckedAllBasketItem: Boolean)

        fun createOrderActCallback(passValidationCheck: Boolean, selectedBasketItem: ArrayList<BasketItem>)

        fun deleteProductInBasketCallback(deletedItemCount: Int)
    }

    interface Model{

        fun loadBasketProducts(isClear: Boolean)

        fun checkAllBasketProduct(newStatus: Boolean)

        fun calculatePrice()

        fun deleteSelectedItems()

        fun createOrderAct()
    }
}