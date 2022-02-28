package kr.co.younhwan.happybuyer.view.basket

import kr.co.younhwan.happybuyer.data.ProductItem

interface BasketContract{
    interface View{

        fun getAct(): BasketActivity

    }

    interface Model{

        fun loadBasketProduct(isClear: Boolean)
    }
}