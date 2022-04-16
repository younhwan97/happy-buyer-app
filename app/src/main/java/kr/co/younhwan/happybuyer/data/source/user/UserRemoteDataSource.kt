package kr.co.younhwan.happybuyer.data.source.user

import android.util.Log
import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.wished.WishedRemoteDataSource
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object UserRemoteDataSource : UserSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://192.168.0.11/auth/api/user" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

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
                                    val activatedBasket =
                                        if (obj.isNull("activated_basket")) "deactivated" else obj.getString(
                                            "activated_basket"
                                        )

                                    user = UserItem(
                                        userId,
                                        nickname,
                                        point,
                                        activatedBasket
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

    override fun updateUser(
        kakaoAccountId: Long,
        target: String,
        newContent: String,
        updateUserCallback: UserSource.UpdateUserCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // API 서버 주소
                // target : nickname, basket, phone, point, address
                val site =
                    "http://happybuyer.co.kr/auth/api/app/update?id=${kakaoAccountId}&target=${target}&content=${newContent}"

                // 응답
                val request = Request.Builder().url(site).get().build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    isSuccess = json.getBoolean("success")
                }
            }

            job.join()
            updateUserCallback?.onUpdateUser(isSuccess)
        }
    }

    override fun deleteUser(
        kakaoAccountId: Long,
        deleteUserCallback: UserSource.DeleteUserCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {


                deleteUserCallback?.onDeleteUser(isSuccess)
            }
        }
    }
}