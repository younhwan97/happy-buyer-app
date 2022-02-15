package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.data.source.image.SampleImageSource
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.CategoryAdapterContract

/**
 * Presenter
 */

class HomePresenter(
    private val view:HomeContract.View,
    private val imageData: SampleImageRepository,
    private val adapterModel: CategoryAdapterContract.Model,
    private val adapterView: CategoryAdapterContract.View
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









//    override fun loadCategory(){
//
//        // imageRepository.loadImageList()
//
//        override fun loadItems(context: Context, isClear: Boolean) {
//            imageData.getImages(context, object : SampleImageSource.LoadImageCallback {
//                override fun onLoadImages(list: ArrayList<ImageItem>) {
//                    if (isClear) {
//                        adapterModel.clearItem()
//                    }
//
//                    adapterModel.addItems(list)
//                    adapterView.notifyAdapter()
//                }
//            })
//        }
//
////        view.updateCategory(HomeModel.imageList, HomeModel.labelList)
////        view.notifyAdapter()
//    }