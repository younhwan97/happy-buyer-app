package kr.co.younhwan.happybuyer.data.source.order

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.OrderItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object OrderRemoteDataSource : OrderSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun create(kakaoAccountId: Long, orderItem: OrderItem, createCallback: OrderSource.CreateCallback?) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 새로운 데이터 생성을 위한 POST Request 생성
                val jsonData = JSONObject()

                jsonData.put("user_id", kakaoAccountId) // 유저 정보

                jsonData.put("receiver", orderItem.receiver) // 배달 정보
                jsonData.put("phone_number", orderItem.phone)
                jsonData.put("address", orderItem.address)

                jsonData.put("requirement", orderItem.requirement) /// 배달 요청사항
                jsonData.put("point_number", orderItem.pointNumber)
                jsonData.put("detective_handling_method", orderItem.detectiveHandlingMethod)

                jsonData.put("payment", orderItem.payment) // 결제수단

                jsonData.put("original_price", orderItem.originalPrice) // 주문 상품 확인
                jsonData.put("event_price", orderItem.eventPrice)
                jsonData.put("be_paid_price", orderItem.bePaidPrice)

                val orderProducts = JSONArray()
                for(index in 0 until orderItem.orderProducts.size){
                    val temp = JSONObject()
                    temp.put("product_id", orderItem.orderProducts[index].productId)
                    temp.put("count", orderItem.orderProducts[index].countInBasket)
                    orderProducts.put(temp)
                }
                jsonData.put("order_products", orderProducts)


                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    isSuccess = json.getBoolean("success")
                }
            }

            job.join()
            createCallback?.onCreate(isSuccess)
        }
    }
}