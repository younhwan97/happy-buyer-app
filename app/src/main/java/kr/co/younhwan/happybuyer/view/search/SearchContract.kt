package kr.co.younhwan.happybuyer.view.search

interface SearchContract{
    interface View{

        fun getAct() : SearchActivity

        fun createResultActivity(keyword: String)
    }

    interface Model{

        fun createRecentSearch(keyword:String)

        fun loadRecentSearch()

        fun deleteAllRecentSearch()

        fun loadSearchHistory()

        fun loadResultSearch(keyword: String?)

    }
}