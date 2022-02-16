package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

interface CategorySource {

    interface LoadCategoryCallback {

        fun onLoadCategories(list: ArrayList<CategoryItem>)
    }

    fun getCategories(context: Context, loadImageCallback: LoadCategoryCallback?)
}