package kr.co.younhwan.happybuyer.data.source.search

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.SearchItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object SearchRemoteDataSource : SearchSource {
    override fun createRecentSearch(
        kakaoAccountId: Long,
        keyword: String
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = createRecent(kakaoAccountId, keyword)
            }

            job.join()
        }
    }

    override fun readRecentSearch(
        kakaoAccountId: Long,
        readRecentSearchCallback: SearchSource.ReadRecentSearchCallback?
    ) {
        runBlocking {
            val list = ArrayList<SearchItem>()

            val job = GlobalScope.launch {
                list.addAll(readRecent(kakaoAccountId))
            }

            job.join()
            readRecentSearchCallback?.onReadRecentSearch(list)
        }
    }

    override fun deleteRecentSearch(
        kakaoAccountId: Long,
        keyword: String?,
        deleteRecentSearchCallback: SearchSource.DeleteRecentSearchCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = deleteRecent(kakaoAccountId, keyword)
            }

            job.join()
            deleteRecentSearchCallback?.onDeleteRecentSearch(isSuccess)
        }
    }

    override fun readSearchHistory(readSearchHistoryCallback: SearchSource.ReadSearchHistoryCallback?) {
        runBlocking {
            val list = ArrayList<String>()

            val job = GlobalScope.launch {
                list.addAll(readHistory())
            }

            job.join()
            readSearchHistoryCallback?.onReadSearchHistory(list)

        }
    }
}

suspend fun createRecent(kakaoAccountId: Long, keyword: String): Boolean {
    var isSuccess = false

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/search/api/app/recent/create?id=${kakaoAccountId}&keyword=${keyword}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        isSuccess = json.getBoolean("success")
    }

    return isSuccess
}

suspend fun readRecent(kakaoAccountId: Long): ArrayList<SearchItem> {
    val list = ArrayList<SearchItem>()

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/search/api/app/recent/read?id=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        val success = json.getBoolean("success")

        if(success){
            val data = JSONArray(json["data"].toString())

            for(i in 0 until data.length()){
                val obj = data.getJSONObject(i)
                val userId = obj.getLong("user_id")
                val keyword = obj.getString("keyword")

                if(userId == kakaoAccountId){
                    list.add(SearchItem(
                        userId = userId,
                        keyword = keyword
                    ))
                }
            }
        }
    }

    return list
}

suspend fun deleteRecent(kakaoAccountId: Long, keyword: String?): Boolean{
    var success = false

    // 클라이언트 생성
    val client = OkHttpClient()

    val site = if(keyword.isNullOrEmpty()){
        "http://happybuyer.co.kr/search/api/app/recent/delete?id=${kakaoAccountId}"
    } else {
        "http://happybuyer.co.kr/search/api/app/recent/delete?id=${kakaoAccountId}&keyword=${keyword}"
    }

    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        success = json.getBoolean("success")
    }

    return success
}

suspend fun readHistory(): ArrayList<String>{
    val list = ArrayList<String>()

    // 클라이언트 생성
    val client = OkHttpClient()
    
    // 요청
    val site = "http://happybuyer.co.kr/search/api/app/history/read"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        val success = json.getBoolean("success")

        if(success){
            val data = JSONArray(json["data"].toString())

            for(i in 0 until data.length()){
                val obj = data.getJSONObject(i)
                val keyword = obj.getString("keyword")
                list.add(keyword)
            }
        }
    }


    return list
}