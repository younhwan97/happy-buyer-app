package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.source.category.CategoryRepository
import kr.co.younhwan.happybuyer.data.source.category.CategorySource
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.HomeAdapterContract

/**
 * Presenter
 */

class HomePresenter(
    private val view: HomeContract.View,
    private val categoryData: CategoryRepository,
    private val adapterModel: HomeAdapterContract.Model,
    private val adapterView: HomeAdapterContract.View
) : HomeContract.Presenter {

    init {
        adapterView.onClickFuncCategoryItem = { i: Int ->
            onClickListenerCategoryItem(i)
        }
    }

    override fun loadItems(context: Context, isClear: Boolean) {
        // 프래그먼트가 그려질 때 "초기에" 호출되는 메서드
        // 레파지토리를 통해 카테고리 목록, 이미지, 타이틀을 가져오고 리사이클러뷰를 그린다.
        categoryData.getCategories(context, object : CategorySource.LoadCategoryCallback {
            override fun onLoadCategories(list: ArrayList<CategoryItem>) {
                if (isClear) adapterModel.clearItem()

                view.setCategoryLabelList(list)
                adapterModel.addItems(list)
                adapterView.notifyAdapter()
            }
        })
    }

    private fun onClickListenerCategoryItem(position: Int) {
        view.createCategoryActivity(position)
    }
}