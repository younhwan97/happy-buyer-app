package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.category.CategoryActivity

interface CategoryContract{
    interface View{

        fun getAct() : CategoryActivity

        fun createProductInWishedResultCallback(perform: String?)

        fun createProductInBasketResultCallback(count: Int)

        fun createLoginActivity()

        fun createProductActivity(productItem: ProductItem)
    }

    interface Model{

        fun loadProducts(isClear: Boolean, selectedCategory: String)

    }
}