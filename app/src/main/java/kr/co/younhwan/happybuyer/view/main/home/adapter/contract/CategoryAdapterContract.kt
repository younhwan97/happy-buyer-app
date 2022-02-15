package kr.co.younhwan.happybuyer.view.main.home.adapter.contract

import kr.co.younhwan.happybuyer.data.ImageItem

interface CategoryAdapterContract {
    interface View {

        var onClickFunc : ((Int) -> Unit)?

        fun notifyAdapter()
    }

    interface Model {

        fun addItems(imageItems: ArrayList<ImageItem>)

        fun clearItem()

        fun getItem(position: Int): ImageItem

    }
}