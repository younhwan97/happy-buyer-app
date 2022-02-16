package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.data.source.image.SampleImageSource
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.HomeAdapterContract
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomeContract

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
    private val adapterModel: CategoryAdapterContract.Model,
    private val adapterView: CategoryAdapterContract.View
) : CategoryContract.Model {

    override fun loadProductItems(context: Context, isClear: Boolean, selectedCategory: String) {
        productData.getImages(context, selectedCategory ,object : ProductSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<CategoryItem>) {
                if (isClear) {
                    adapterModel.clearItem()
                }
                Log.d("test", "$list")

                adapterModel.addItems(list)
                adapterView.notifyAdapter()
            }
        })
    }
}