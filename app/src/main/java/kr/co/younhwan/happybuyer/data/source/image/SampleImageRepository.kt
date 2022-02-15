package kr.co.younhwan.happybuyer.data.source.image

import android.content.Context
import kr.co.younhwan.happybuyer.data.ImageItem

object SampleImageRepository : SampleImageSource {

    private val sampleImageLocalDataSource = SampleImageLocalDataSource

    override fun getImages(context: Context, loadImageCallback: SampleImageSource.LoadImageCallback?) {
        sampleImageLocalDataSource.getImages(context, object : SampleImageSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<ImageItem>) {
                loadImageCallback?.onLoadImages(list)
            }
        })
    }
}