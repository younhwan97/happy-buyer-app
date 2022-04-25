package kr.co.younhwan.happybuyer.view.search.adapter.recent.contract

import kr.co.younhwan.happybuyer.data.RecentItem

interface RecentAdapterContract {
    interface View{

        var onClickFuncOfDeleteBtn: ((String, Int) -> Unit)?

        var onClickFuncOfRecentSearch: ((String) -> Unit)?

        fun notifyAdapter()

        fun notifyRemoved(position: Int)
    }

    interface Model{

        fun addItems(recentItems: ArrayList<RecentItem>)

        fun clearItem()

        fun getItem(position: Int): RecentItem

        fun getItemCount(): Int

        fun deleteItem(position: Int)
    }
}