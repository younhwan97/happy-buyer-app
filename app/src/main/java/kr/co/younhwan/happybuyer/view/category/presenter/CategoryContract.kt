package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.category.CategoryActivity

interface CategoryContract{
    interface View{

        fun getAct() : CategoryActivity

        fun createProductInWishedResultCallback(explain: String)

        fun createProductInBasketResultCallback(isSuccess: Boolean)

        fun createLoginActivity()

    }

    interface Model{

        fun loadProducts(isClear: Boolean, selectedCategory: String)

    }
}