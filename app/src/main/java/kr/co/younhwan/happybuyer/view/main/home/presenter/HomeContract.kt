package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.MainActivity

interface HomeContract {
    interface View {

        fun getAct(): MainActivity

        fun loadCategoriesCallback(list: ArrayList<CategoryItem>)

        fun loadEventProductsCallback(resultCount: Int)

        fun loadPopularProductsCallback(resultCount: Int)

        fun createCategoryAct(adapterPosition: Int)

        fun createLoginAct()

        fun createProductAct(productItem: ProductItem)

        fun createOrUpdateProductInBasketCallback(resultCount: Int)

    }

    interface Presenter {

        fun loadCategories(isClear: Boolean, context: Context)

        fun loadEventProducts(isClear: Boolean)

        fun loadPopularProducts(isClear: Boolean)

    }
}