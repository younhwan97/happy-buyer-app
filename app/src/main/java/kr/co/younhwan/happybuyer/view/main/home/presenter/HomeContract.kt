package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.data.ImageItem

interface HomeContract {
    interface View {
        // View method
        fun setCategoryLabel(list: ArrayList<ImageItem>)

        fun createCategoryActivity(adapterPosition: Int)
    }

    interface Presenter {
        // Presenter method
        fun loadItems(context: Context, isClear: Boolean)
    }
}