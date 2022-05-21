package kr.co.younhwan.happybuyer.view.category.presenter

import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.category.CategoryActivity

interface CategoryContract {
    interface View {

        fun getAct(): CategoryActivity

        fun loadProductsCallback(resultCount: Int)

        fun createLoginAct()

        fun createProductAct(productItem: ProductItem)

        fun createOrUpdateProductInBasketCallback(resultCount: Int)

    }

    interface Model {

        fun loadProducts(isClear: Boolean, selectedCategory: String, sortBy: String, page: Int)

        fun loadMoreProducts(selectedCategory: String, sortBy: String, page: Int)

    }
}