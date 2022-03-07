package kr.co.younhwan.happybuyer.view.search

interface SearchContract{
    interface View{

        fun getAct() : SearchActivity
    }

    interface Model{

        fun loadRecentSearch()

        fun loadSearchHistory()

        fun deleteAllRecentSearch()

        fun createRecentSearch(keyword:String)
    }
}