package kr.co.younhwan.happybuyer.view.main.home.adapter.main.contract

import kr.co.younhwan.happybuyer.data.CategoryItem

interface HomeAdapterContract {
    interface View {

        var onClickFuncOfCategoryItem : ((Int) -> Unit)?

        fun notifyAdapter()

    }

    interface Model {

        fun addItems(categoryItems: ArrayList<CategoryItem>)

        fun clearItem()

        fun getItem(position: Int): CategoryItem

    }
}