package kr.co.younhwan.happybuyer.data.source.image

import android.content.Context
import kr.co.younhwan.happybuyer.data.ImageItem

interface SampleImageSource {

    interface LoadImageCallback {

        fun onLoadImages(list: ArrayList<ImageItem>)
    }

    fun getImages(context: Context, loadImageCallback: LoadImageCallback?)
}