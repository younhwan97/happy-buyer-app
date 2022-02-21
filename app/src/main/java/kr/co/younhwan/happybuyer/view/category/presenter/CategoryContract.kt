package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.category.CategoryActivity

interface CategoryContract{
    interface View{

        fun getAct() : CategoryActivity

        fun addWishedResultCallback(explain: String)

        fun createLoginActivity()

    }

    interface Model{

        fun loadProductItems(context: Context, isClear: Boolean, selectedCategory: String)

    }
}