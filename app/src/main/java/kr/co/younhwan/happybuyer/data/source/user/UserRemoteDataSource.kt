package kr.co.younhwan.happybuyer.data.source.user

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.UserItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object UserRemoteDataSource : UserSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/auth/api/user" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    // CREATE
    override fun create(
        kakaoAccountId: Long,
        kakaoNickname: String?,
        createCallback: UserSource.CreateCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터 생성을 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("id", kakaoAccountId)
                jsonData.put("nickname", kakaoNickname)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        createCallback?.onCreate(isSuccess)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()
                        val json = JSONObject(resultText)

                        isSuccess = json.getBoolean("success")
                        createCallback?.onCreate(isSuccess)
                    }
                })
            }
        }
    }

    // READ
    override fun read(
        kakaoAccountId: Long,
        readCallback: UserSource.ReadCallback?
    ) {
        runBlocking {
            var user: UserItem? = null

            launch {
                // API 서버 주소
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 얻기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        readCallback?.onRead(user)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()
                        val json = JSONObject(resultText)

                        val success = json.getBoolean("success")
                        if (success) {
                            val data = JSONArray(json["data"].toString())

                            for (i in 0 until data.length()) {
                                val obj = data.getJSONObject(i)
                                val userId = obj.getLong("id")

                                if (kakaoAccountId == userId) {
                                    val nickname = obj.getString("name")
                                    val point = obj.getString("point")

                                    user = UserItem(
                                        userId,
                                        nickname,
                                        point
                                    )

                                    break
                                }
                            }
                        }
                        readCallback?.onRead(user)
                    }
                })
            }
        }
    }

    // UPDATE
    override fun update(
        kakaoAccountId: Long,
        updateTarget: String,
        newContent: String,
        updateCallback: UserSource.UpdateCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터 수정을 위한 PUT Request 생성
                val jsonData = JSONObject()
                jsonData.put("id", kakaoAccountId)
                jsonData.put("update_target", updateTarget)
                jsonData.put("new_content", newContent)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).put(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        updateCallback?.onUpdate(isSuccess)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()
                        val json = JSONObject(resultText)

                        isSuccess = json.getBoolean("success")
                        updateCallback?.onUpdate(isSuccess)
                    }
                })
            }
        }
    }
}