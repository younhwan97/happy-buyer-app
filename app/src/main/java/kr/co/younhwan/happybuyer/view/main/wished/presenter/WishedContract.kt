package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface WishedContract{
    interface View {

        fun getAct() : MainActivity

        fun loadWishedProductsCallback(resultCount: Int)

        fun deleteWishedProductCallback(perform: String?, resultCount: Int) // 추후 변경

        fun addBasketResultCallback(count: Int)

    }

    interface Presenter{

        fun loadWishedProducts(isClear: Boolean)

    }
}