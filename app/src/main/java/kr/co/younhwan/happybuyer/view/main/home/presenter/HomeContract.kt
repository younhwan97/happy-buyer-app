package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface HomeContract {
    interface View {
        // View method
        fun setCategoryLabelList(list: ArrayList<CategoryItem>)

        fun createCategoryActivity(adapterPosition: Int)

        fun createLoginActivity()

        fun createProductActivity(productItem: ProductItem)

        fun createProductInBasketResultCallback(count: Int)

        fun getAct() : MainActivity
    }

    interface Presenter {
        // Presenter method
        fun loadCategories(context: Context, isClear: Boolean)

        fun loadEventProduct(isClear: Boolean)

        fun loadPopularProduct(isClear: Boolean)
    }
}