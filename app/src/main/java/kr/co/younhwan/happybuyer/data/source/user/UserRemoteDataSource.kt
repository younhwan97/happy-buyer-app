package kr.co.younhwan.happybuyer.data.source.user

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object UserRemoteDataSource : UserSource {


    override fun createUser(
        kakaoLoginId: Long?,
        kakaoNickname: String?,
        createUserCallback: UserSource.createUserCallback?
    ) {

        runBlocking {
            var isSuccess = false
            val job = GlobalScope.launch {
                isSuccess = temp(kakaoLoginId, kakaoNickname)
            }
            job.join()
            createUserCallback?.onCreateUser(isSuccess)
        }
    }
}

suspend fun temp(kakaoAccountId: Long?, kakaoAccountNickname: String?): Boolean {
    // 클라이언트 만들기
    val client = OkHttpClient()

    // 요청
    val site = "http://192.168.0.11/auth/api/app/create?id=${kakaoAccountId}&nickname=${kakaoAccountNickname}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()
    var isSuccess: Boolean = false
    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        isSuccess = json.getBoolean("success")
    }
    return isSuccess
}