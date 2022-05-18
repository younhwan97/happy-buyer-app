package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.data.ProductItem

interface SearchContract{
    interface View{

        fun getAct() : SearchActivity

        fun loadSearchResultCallback(resultCount: Int)

        fun sortSearchResultCallback()

        fun createResultActivity(keyword: String)

        fun createProductActivity(productItem: ProductItem)
    }

    interface Model{

        fun createRecentWithHistory(keyword:String)

        fun loadRecent()

        fun deleteAllRecent()

        fun loadSearchHistory()

        fun loadResultSearch(isClear:Boolean, keyword: String?, page:Int)

        fun loadMoreResultSearch(keyword: String?, page:Int)

        fun sortSearchResult(newItem: String)
    }
}