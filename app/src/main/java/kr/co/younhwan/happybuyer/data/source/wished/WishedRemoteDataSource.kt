package kr.co.younhwan.happybuyer.data.source.wished

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object WishedRemoteDataSource : WishedSource{
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/wished/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun createOrDeleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        createOrDeleteProductCallback: WishedSource.CreateOrDeleteProductCallback?
    ) {
        runBlocking {
            var perform: String? = null

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터 생성, 데이터 삭제 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("uid", kakaoAccountId)
                jsonData.put("pid", productId)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    val success = json.getBoolean("success")
                    val resultPerform = json.getString("perform")

                    if (success)
                        perform = resultPerform
                }
            }

            job.join()
            createOrDeleteProductCallback?.onCreateOrDeleteProduct(perform)
        }
    }

    override fun readProductsId(
        kakaoAccountId: Long,
        readProductsIdCallback: WishedSource.ReadProductsIdCallback?
    ) {
        runBlocking {
            val list = ArrayList<Int>()

            val job = GlobalScope.launch {
                // 요청
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 읽기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)

                    val success = json.getBoolean("success")
                    if (success) { // 유저가 찜한 상품이 있을 때
                        val data = JSONArray(json["data"].toString())
                        for (i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                            val userId = obj.getLong("user_id")

                            if (kakaoAccountId == userId) {
                                list.add(obj.getInt("product_id"))
                            }
                        }
                    }
                }
            }

            job.join()
            readProductsIdCallback?.onReadProductsId(list)
        }
    }
}