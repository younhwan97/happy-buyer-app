package kr.co.younhwan.happybuyer.data.source.user

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.UserItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object UserRemoteDataSource : UserSource {

    override fun createUser(
        kakaoAccountId: Long,
        kakaoNickname: String?,
        createUserCallback: UserSource.createUserCallback?
    ) {

        runBlocking {
            var isSuccess = false
            val job = GlobalScope.launch {
                isSuccess = create(kakaoAccountId, kakaoNickname)
            }
            job.join()
            createUserCallback?.onCreateUser(isSuccess)
        }
    }

    override fun updateUser(
        kakaoAccountId: Long,
        newNickname: String,
        updateUserCallback: UserSource.updateUserCallback?
    ) {

        runBlocking {
            var isSuccess = false
            val job = GlobalScope.launch {
                isSuccess = update(kakaoAccountId, newNickname)
            }

            job.join()
            updateUserCallback?.onUpdateUser(isSuccess)
        }
    }

    override fun readUser(
        kakaoAccountId: Long,
        readUserCallback: UserSource.readUserCallback?
    ) {
        runBlocking {
            var user: UserItem? = null

            val job = GlobalScope.launch {
                user = read(kakaoAccountId)
            }

            job.join()
            readUserCallback?.onReadUser(user)
        }
    }
}

suspend fun create(kakaoAccountId: Long?, kakaoAccountNickname: String?): Boolean {
    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/auth/api/app/create?id=${kakaoAccountId}&nickname=${kakaoAccountNickname}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()
    var isSuccess = false

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        isSuccess = json.getBoolean("success")
    }

    return isSuccess
}

suspend fun update(kakaoAccountId: Long, newNickname: String): Boolean {
    val client = OkHttpClient()

    val site =
        "http://happybuyer.co.kr/auth/api/app/update?id=${kakaoAccountId}&nickname=${newNickname}"
    val request = Request.Builder().url(site).get().build()

    val response = client.newCall(request).execute()
    var isSuccess: Boolean = false

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        isSuccess = json.getBoolean("success")
    }

    return isSuccess
}

suspend fun read(kakaoAccountId: Long): UserItem? {
    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/auth/api/app/read?id=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()
    
    // 응답
    val response = client.newCall(request).execute()
    
    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        val data = JSONArray(json["data"].toString()).getJSONObject(0)

        val id = if (data.isNull("id")) 0L else data.getLong("id")

        if (kakaoAccountId == id) {
            val nickname = if (data.isNull("name")) "-" else data.getString("name")
            val pointNumber = if (data.isNull("point_number")) 0 else data.getInt("point_number")
            val shippingAddress =
                if (data.isNull("shipping_address")) "-" else data.getString("shipping_address")

            return UserItem(id, nickname, pointNumber, shippingAddress)
        }
    }
    
    // 서버에 문제가 있어 응답을 받지 못한 경우
    return null
}