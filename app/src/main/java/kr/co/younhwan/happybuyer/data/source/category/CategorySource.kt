package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

interface CategorySource {

    interface ReadCategoryCallback {

        fun onReadCategories(list: ArrayList<CategoryItem>)
    }

    fun readCategories(context: Context, loadImageCallback: ReadCategoryCallback?)
}