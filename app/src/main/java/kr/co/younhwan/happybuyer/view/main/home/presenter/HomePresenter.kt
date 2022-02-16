package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.data.source.image.SampleImageSource
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.HomeAdapterContract

/**
 * Presenter
 */

class HomePresenter(
    private val view:HomeContract.View,
    private val imageData: SampleImageRepository,
    private val adapterModel: HomeAdapterContract.Model,
    private val adapterView: HomeAdapterContract.View
    ) : HomeContract.Presenter {

    init {
        adapterView.onClickFunc = { i: Int ->
            onClickListener(i)
        }
    }

    override fun loadItems(context: Context, isClear: Boolean) {
        imageData.getImages(context, object : SampleImageSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<ImageItem>) {
                if (isClear) {
                    adapterModel.clearItem()
                }

                view.setCategoryLabel(list)
                adapterModel.addItems(list)
                adapterView.notifyAdapter()
            }
        })
    }

    private fun onClickListener(position: Int) {
        view.createCategoryActivity(position)
    }
}