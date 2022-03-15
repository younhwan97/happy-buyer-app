package kr.co.younhwan.happybuyer.view.basket

interface BasketContract{
    interface View{

        fun getAct(): BasketActivity

        fun loadBasketProductCallback(size: Int, totalPrice: Int)

    }

    interface Model{

        fun loadBasketProduct(isClear: Boolean)

        fun checkAllBasketProduct()
    }
}