package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface WishedContract{
    interface View {

        fun getAct() : MainActivity

        fun setEmpty()

        fun deleteWishedResultCallback(perform: String?)

        fun addBasketResultCallback(count: Int)
    }

    interface Presenter{
        fun loadWishedItem(context: Context, isClear: Boolean)
    }
}