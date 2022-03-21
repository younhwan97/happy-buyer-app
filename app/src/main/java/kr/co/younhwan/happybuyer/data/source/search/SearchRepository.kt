package kr.co.younhwan.happybuyer.data.source.search

import kr.co.younhwan.happybuyer.data.RecentItem

object SearchRepository : SearchSource{

    private val searchRemoteDataSource = SearchRemoteDataSource

    override fun createRecentWithHistory(
        kakaoAccountId: Long,
        keyword: String,

    ) {
        searchRemoteDataSource.createRecentWithHistory(kakaoAccountId, keyword)
    }

    override fun readRecent(
        kakaoAccountId: Long,
        readRecentCallback: SearchSource.ReadRecentCallback?
    ) {
        searchRemoteDataSource.readRecent(kakaoAccountId, object : SearchSource.ReadRecentCallback{
            override fun onReadRecent(list: ArrayList<RecentItem>) {
                readRecentCallback?.onReadRecent(list)
            }
        })
    }

    override fun readHistory(
        readHistoryCallback: SearchSource.ReadHistoryCallback?
    ) {
        searchRemoteDataSource.readHistory(object : SearchSource.ReadHistoryCallback{
            override fun onReadHistory(list: ArrayList<String>) {
                readHistoryCallback?.onReadHistory(list)
            }
        })
    }

    override fun deleteRecent(
        kakaoAccountId: Long,
        keyword: String?,
        deleteRecentCallback: SearchSource.DeleteRecentCallback?
    ) {
        searchRemoteDataSource.deleteRecent(kakaoAccountId, keyword, object : SearchSource.DeleteRecentCallback{
            override fun onDeleteRecent(isSuccess: Boolean) {
                deleteRecentCallback?.onDeleteRecent(isSuccess)
            }
        })
    }
}