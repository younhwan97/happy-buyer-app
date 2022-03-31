package kr.co.younhwan.happybuyer.data.source.order

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object OrderRemoteDataSource : OrderSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://192.168.0.11/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun create(
        kakaoAccountId: Long,
        orderItem: OrderItem,
        createCallback: OrderSource.CreateCallback?
    ) {
        runBlocking {
            var orderId = -1

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 새로운 데이터 생성을 위한 POST Request 생성
                val jsonData = JSONObject()

                jsonData.put("user_id", kakaoAccountId) // 주문 정보
                jsonData.put("name", orderItem.name)
                jsonData.put("status", orderItem.status)
                // jsonData.put("date", orderItem.date)

                jsonData.put("receiver", orderItem.receiver) // 배달 정보
                jsonData.put("phone", orderItem.phone)
                jsonData.put("address", orderItem.address)

                jsonData.put("requirement", orderItem.requirement) /// 배달 요청사항
                jsonData.put("point", orderItem.point)
                jsonData.put("detective_handling_method", orderItem.detectiveHandlingMethod)

                jsonData.put("payment", orderItem.payment) // 결제수단

                jsonData.put("original_price", orderItem.originalPrice) // 주문 상품 확인
                jsonData.put("event_price", orderItem.eventPrice)
                jsonData.put("be_paid_price", orderItem.bePaidPrice)

                val products = JSONArray()
                for (index in 0 until orderItem.products.size) {
                    val product = JSONObject()
                    product.put("product_id", orderItem.products[index].productId)
                    product.put("count", orderItem.products[index].countInBasket)
                    products.put(product)
                }
                jsonData.put("products", products)

                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    val success = json.getBoolean("success")

                    if (success) {
                        orderId = json.getInt("order_id")
                    }
                }
            }

            job.join()
            createCallback?.onCreate(orderId)
        }
    }

    override fun read(kakaoAccountId: Long, readCallback: OrderSource.ReadCallback?) {
        runBlocking {
            val list = ArrayList<OrderItem>()

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 읽어오기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)

                    val success = json.getBoolean("success")
                    if (success) {
                        val data = JSONArray(json["data"].toString())

                        for (i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                            val userId = obj.getLong("user_id")

                            if (userId == kakaoAccountId) {
                                val orderId = obj.getInt("order_id")
                                val name = obj.getString("name")
                                val status = obj.getString("status")
                                val date = obj.getString("date")

                                val receiver = obj.getString("receiver")
                                val phone = obj.getString("phone")
                                val address = obj.getString("address")

                                val requirement = obj.getString("requirement")
                                val point = obj.getString("point")
                                val detectiveHandlingMethod =
                                    obj.getString("detective_handling_method")

                                val payment = obj.getString("payment")

                                val originalPrice = obj.getString("original_price")
                                val eventPrice = obj.getString("event_price")
                                val bePaidPrice = obj.getString("be_paid_price")

                                list.add(
                                    OrderItem(
                                        orderId = orderId,
                                        name = name,
                                        status = status,
                                        date = date,
                                        receiver = receiver,
                                        phone = phone,
                                        address = address,
                                        requirement = requirement,
                                        point = point,
                                        detectiveHandlingMethod = detectiveHandlingMethod,
                                        payment = payment,
                                        originalPrice = originalPrice,
                                        eventPrice = eventPrice,
                                        bePaidPrice = bePaidPrice,
                                        products = ArrayList<BasketItem>()
                                    )
                                )
                            }
                        }
                    }
                }
            }

            job.join()
            readCallback?.onRead(list)
        }
    }
}