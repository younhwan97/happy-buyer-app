package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context

interface CategoryContract{
    interface View{
    }

    interface Model{

        fun loadProductItems(context: Context, isClear: Boolean, selectedCategory: String)

    }
}