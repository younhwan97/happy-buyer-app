package kr.co.younhwan.happybuyer.data.source.category

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem

object CategoryRepository : CategorySource {

    private val categoryLocalDataSource = CategoryLocalDataSource

    override fun readCategories(context: Context, loadImageCallback: CategorySource.ReadCategoryCallback?) {
        categoryLocalDataSource.readCategories(context, object : CategorySource.ReadCategoryCallback {
            override fun onReadCategories(list: ArrayList<CategoryItem>) {
                loadImageCallback?.onReadCategories(list)
            }
        })
    }
}