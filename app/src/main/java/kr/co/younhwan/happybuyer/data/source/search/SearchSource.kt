package kr.co.younhwan.happybuyer.data.source.search

import kr.co.younhwan.happybuyer.data.SearchItem

interface SearchSource{

    // CREATE
    fun createRecentSearch(kakaoAccountId:Long, keyword:String)

    // READ
    interface ReadRecentSearchCallback{
        fun onReadRecentSearch(list: ArrayList<SearchItem>)
    }

    fun readRecentSearch(kakaoAccountId: Long, readRecentSearchCallback: ReadRecentSearchCallback?)

    // UPDATE
    // Not implemented..

    // DELETE
    interface DeleteRecentSearchCallback{
        fun onDeleteRecentSearch(isSuccess: Boolean)
    }

    fun deleteRecentSearch(kakaoAccountId: Long, keyword: String?, deleteRecentSearchCallback: DeleteRecentSearchCallback?)

    interface ReadSearchHistoryCallback{
        fun onReadSearchHistory(list: ArrayList<String>)
    }

    fun readSearchHistory(
        readSearchHistoryCallback: ReadSearchHistoryCallback?
    )
}