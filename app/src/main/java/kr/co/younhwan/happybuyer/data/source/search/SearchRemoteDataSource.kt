package kr.co.younhwan.happybuyer.data.source.search

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.RecentItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object SearchRemoteDataSource : SearchSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/search/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun createRecentWithHistory(
        kakaoAccountId: Long,
        keyword: String
    ) {
        runBlocking {
            launch {
                // API 서버 주소
                val site = "${serverInfo}/recent-with-history"

                // 새로운 데이터 생성을 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("id", kakaoAccountId)
                jsonData.put("keyword", keyword)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                    }
                })
            }
        }
    }

    override fun readRecent(
        kakaoAccountId: Long,
        readRecentCallback: SearchSource.ReadRecentCallback?
    ) {
        runBlocking {
            val list = ArrayList<RecentItem>()

            launch {
                // API 서버 주소
                val site = "${serverInfo}/recent?id=${kakaoAccountId}"

                // 데이터를 읽어오기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            readRecentCallback?.onReadRecent(list)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        val success = json.getBoolean("success")

                        if (success) {
                            val data = JSONArray(json["data"].toString())

                            for (i in 0 until data.length()) {
                                val obj = data.getJSONObject(i)
                                val userId = obj.getLong("user_id")
                                val keyword = obj.getString("keyword")

                                if (userId == kakaoAccountId) {
                                    list.add(
                                        RecentItem(
                                            userId = userId,
                                            keyword = keyword
                                        )
                                    )
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            readRecentCallback?.onReadRecent(list)
                        }
                    }
                })
            }
        }
    }

    override fun readHistory(readHistoryCallback: SearchSource.ReadHistoryCallback?) {
        runBlocking {
            val list = ArrayList<String>()

            launch {
                // API 서버 주소
                val site = "${serverInfo}/history"

                // 데이터를 읽어오기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            readHistoryCallback?.onReadHistory(list)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        val success = json.getBoolean("success")

                        if (success) {
                            val data = JSONArray(json["data"].toString())

                            for (i in 0 until data.length()) {
                                val obj = data.getJSONObject(i)
                                val keyword = obj.getString("keyword")
                                list.add(keyword)
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            readHistoryCallback?.onReadHistory(list)
                        }
                    }
                })
            }
        }
    }

    override fun deleteRecent(
        kakaoAccountId: Long,
        keyword: String?,
        deleteRecentCallback: SearchSource.DeleteRecentCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {
                // API 서버 주소
                val site = "${serverInfo}/recent"

                // 새로운 데이터 삭제를 위한 DELETE Request 생성
                val jsonData = JSONObject()
                jsonData.put("id", kakaoAccountId)
                if (!keyword.isNullOrEmpty()) {
                    jsonData.put("keyword", keyword)
                }
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).delete(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            deleteRecentCallback?.onDeleteRecent(isSuccess)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        isSuccess = json.getBoolean("success")

                        CoroutineScope(Dispatchers.Main).launch {
                            deleteRecentCallback?.onDeleteRecent(isSuccess)
                        }
                    }
                })
            }
        }
    }
}