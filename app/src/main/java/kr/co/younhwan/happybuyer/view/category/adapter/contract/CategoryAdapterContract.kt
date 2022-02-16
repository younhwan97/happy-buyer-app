package kr.co.younhwan.happybuyer.view.category.adapter.contract

import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ImageItem

interface CategoryAdapterContract{
    interface View{

        fun notifyAdapter()

    }

    interface Model{

        fun addItems(categoryItems: ArrayList<CategoryItem>)

        fun clearItem()

        fun getItem(position: Int): CategoryItem

    }

}