package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

object CategoryRepository : CategorySource {

    private val categoryLocalDataSource = CategoryLocalDataSource

    override fun getCategories(context: Context, loadImageCallback: CategorySource.LoadCategoryCallback?) {
        categoryLocalDataSource.getCategories(context, object : CategorySource.LoadCategoryCallback {
            override fun onLoadCategories(list: ArrayList<CategoryItem>) {
                loadImageCallback?.onLoadCategories(list)
            }
        })
    }
}