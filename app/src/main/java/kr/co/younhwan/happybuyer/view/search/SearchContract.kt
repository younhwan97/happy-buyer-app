package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.data.ProductItem

interface SearchContract{
    interface View{

        fun getAct() : SearchActivity

        fun loadSearchResultCallback(size: Int)

        fun sortSearchResultCallback()

        fun createResultActivity(keyword: String)

        fun createProductActivity(productItem: ProductItem)

        fun createBasketActivity()
    }

    interface Model{

        fun createRecentWithHistory(keyword:String)

        fun loadRecent()

        fun deleteAllRecent()

        fun loadSearchHistory()

        fun loadResultSearch(keyword: String?)

        fun sortSearchResult(newItem: String)
    }
}