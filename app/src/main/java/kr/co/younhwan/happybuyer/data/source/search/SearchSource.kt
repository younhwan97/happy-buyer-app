package kr.co.younhwan.happybuyer.data.source.search

import kr.co.younhwan.happybuyer.data.RecentItem

interface SearchSource{
    fun createRecentWithHistory(kakaoAccountId:Long, keyword:String)

    fun readRecent(kakaoAccountId: Long, readRecentCallback: ReadRecentCallback?)

    interface ReadRecentCallback{
        fun onReadRecent(list: ArrayList<RecentItem>)
    }

    fun readHistory(readHistoryCallback: ReadHistoryCallback?)

    interface ReadHistoryCallback{
        fun onReadHistory(list: ArrayList<String>)
    }

    fun deleteRecent(kakaoAccountId: Long, keyword: String?, deleteRecentCallback: DeleteRecentCallback?)

    interface DeleteRecentCallback{
        fun onDeleteRecent(isSuccess: Boolean)
    }
}