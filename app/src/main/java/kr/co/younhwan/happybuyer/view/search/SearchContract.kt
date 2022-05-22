package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.data.ProductItem

interface SearchContract {
    interface View {

        fun getAct(): SearchActivity

        fun loadSearchResultCallback(resultCount: Int)

        fun createResultActivity(keyword: String)

        fun createProductActivity(productItem: ProductItem)

    }

    interface Model {

        fun createRecentWithHistory(keyword: String)

        fun loadRecent()

        fun loadSearchHistory()

        fun loadSearchResult(isClear: Boolean, keyword: String?, sortBy: String?, page: Int)

        fun loadMoreSearchResult(keyword: String?, sortBy: String?, page: Int)

        fun deleteAllRecent()

    }
}