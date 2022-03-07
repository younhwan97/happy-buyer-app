package kr.co.younhwan.happybuyer.view.search.adapter.suggested.contract

interface SuggestedAdapterContract {
    interface View {
        fun notifyAdapter()
    }

    interface Model {
        fun addItems(suggestedItems: ArrayList<String>)

        fun addItemsOnHistoryItemList(searchHistoryItems: ArrayList<String>)

        fun clearItem()

        fun getItem(position: Int): String

        fun getItemCount(): Int
    }
}