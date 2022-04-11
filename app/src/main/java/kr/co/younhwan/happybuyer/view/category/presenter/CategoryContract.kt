package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.category.CategoryActivity

interface CategoryContract{
    interface View{

        fun getAct() : CategoryActivity

        fun createProductInBasketResultCallback(count: Int)

        fun createLoginActivity()

        fun createProductActivity(productItem: ProductItem)

        fun loadProductsCallback(resultCount: Int)
    }

    interface Model{

        fun loadProducts(isClear: Boolean, selectedCategory: String, page:Int)

        fun loadMoreProducts(selectedCategory: String, page: Int)

    }
}