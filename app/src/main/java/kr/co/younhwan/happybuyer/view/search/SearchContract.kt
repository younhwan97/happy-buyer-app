package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.data.ProductItem

interface SearchContract{
    interface View{

        fun getAct() : SearchActivity

        fun createResultActivity(keyword: String)

        fun createProductActivity(productItem: ProductItem)

        fun createBasketActivity()

        fun loadResultSearchCallback(size: Int)

        fun sortResultSearchCallback()
    }

    interface Model{

        fun createRecentSearch(keyword:String)

        fun loadRecentSearch()

        fun deleteAllRecentSearch()

        fun loadSearchHistory()

        fun loadResultSearch(keyword: String?)

        fun sortResultSearch(newItem: String)

    }
}