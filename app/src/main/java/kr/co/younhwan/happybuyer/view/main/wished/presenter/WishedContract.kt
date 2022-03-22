package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface WishedContract{
    interface View {

        fun getAct() : MainActivity

        fun loadWishedProductsCallback(count: Int)

        fun deleteWishedProductCallback(perform: String?) // 추후 변경

        fun addBasketResultCallback(count: Int)

    }

    interface Presenter{

        fun loadWishedProducts(context: Context, isClear: Boolean)

    }
}