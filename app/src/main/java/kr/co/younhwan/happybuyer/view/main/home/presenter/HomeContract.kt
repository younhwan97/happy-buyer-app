package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface HomeContract {
    interface View {
        fun loadCategoriesCallback(list: ArrayList<CategoryItem>)

        fun loadEventProductsCallback(resultCount: Int)

        fun loadPopularProductsCallback(resultCount: Int)


        fun createCategoryActivity(adapterPosition: Int)

        fun createLoginActivity()

        fun createProductActivity(productItem: ProductItem)

        fun createProductInBasketResultCallback(resultCount: Int)

        fun getAct() : MainActivity
    }

    interface Presenter {
        // Presenter method
        fun loadCategories(isClear: Boolean, context: Context)

        fun loadEventProducts(isClear: Boolean)

        fun loadPopularProducts(isClear: Boolean)
    }
}