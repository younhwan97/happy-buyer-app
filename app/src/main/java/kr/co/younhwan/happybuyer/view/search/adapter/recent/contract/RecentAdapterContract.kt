package kr.co.younhwan.happybuyer.view.search.adapter.recent.contract

import kr.co.younhwan.happybuyer.data.SearchItem

interface RecentAdapterContract {
    interface View{

        var onClickFuncOfDeleteBtn: ((String, Int) -> Unit)?

        fun notifyAdapter()

        fun notifyRemoved(position: Int)
    }

    interface Model{

        fun addItems(recentSearchItems: ArrayList<SearchItem>)

        fun clearItem()

        fun getItem(position: Int): SearchItem

        fun getItemCount(): Int

        fun deleteItem(position: Int)
    }
}