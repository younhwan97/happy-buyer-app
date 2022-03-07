package kr.co.younhwan.happybuyer.data.source.search

import kr.co.younhwan.happybuyer.data.SearchItem

object SearchRepository : SearchSource{

    private val searchRemoteDataSource = SearchRemoteDataSource

    override fun createRecentSearch(
        kakaoAccountId: Long,
        keyword: String,

    ) {
        searchRemoteDataSource.createRecentSearch(kakaoAccountId, keyword)
    }

    override fun readRecentSearch(
        kakaoAccountId: Long,
        readRecentSearchCallback: SearchSource.ReadRecentSearchCallback?
    ) {
        searchRemoteDataSource.readRecentSearch(kakaoAccountId, object : SearchSource.ReadRecentSearchCallback{
            override fun onReadRecentSearch(list: ArrayList<SearchItem>) {
                readRecentSearchCallback?.onReadRecentSearch(list)
            }
        })
    }

    override fun deleteRecentSearch(
        kakaoAccountId: Long,
        keyword: String?,
        deleteRecentSearchCallback: SearchSource.DeleteRecentSearchCallback?
    ) {
        searchRemoteDataSource.deleteRecentSearch(kakaoAccountId, keyword, object : SearchSource.DeleteRecentSearchCallback{
            override fun onDeleteRecentSearch(isSuccess: Boolean) {
                deleteRecentSearchCallback?.onDeleteRecentSearch(isSuccess)
            }
        })
    }


    override fun readSearchHistory(
        readSearchHistoryCallback: SearchSource.ReadSearchHistoryCallback?
    ) {
        searchRemoteDataSource.readSearchHistory(object : SearchSource.ReadSearchHistoryCallback{
            override fun onReadSearchHistory(list: ArrayList<String>) {
                readSearchHistoryCallback?.onReadSearchHistory(list)
            }
        })
    }
}