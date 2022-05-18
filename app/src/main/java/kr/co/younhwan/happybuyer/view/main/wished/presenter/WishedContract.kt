package kr.co.younhwan.happybuyer.view.main.wished.presenter

import kr.co.younhwan.happybuyer.view.main.MainActivity

interface WishedContract {
    interface View {

        fun getAct(): MainActivity

        fun loadWishedProductsCallback(resultCount: Int)

        fun deleteProductInWishedCallback(perform: String?, resultCount: Int)

        fun createOrUpdateProductInBasketCallback(resultCount: Int)

    }

    interface Presenter {

        fun loadWishedProducts(isClear: Boolean)

    }
}