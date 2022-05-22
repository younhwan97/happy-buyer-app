package kr.co.younhwan.happybuyer.data.source.event

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object EventRemoteDataSource : EventSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/event/api" // API 서버

    // READ
    override fun readProducts(
        sortBy:String?,
        page: Int,
        readProductsCallback: EventSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<ProductItem>()

            launch {
                // API 서버 주소
                var site = "$serverInfo?page=${page}"

                if (!sortBy.isNullOrEmpty()) {
                    site += "&sort=${sortBy}"
                }

                // 데이터를 읽기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            readProductsCallback?.onReadProducts(list)
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
                                val productStatus = obj.getString("status")
                                // val productCategory = obj.getString("category")

                                if (productStatus == "판매중") {
                                    val productId = obj.getInt("product_id")
                                    val productName = obj.getString("name")
                                    val productPrice = obj.getInt("price")
                                    val productImage = obj.getString("image_url")
                                    val eventPrice = obj.getInt("event_price")

                                    list.add(
                                        ProductItem(
                                            productId = productId,
                                            productImageUrl = productImage,
                                            productName = productName,
                                            productPrice = productPrice,
                                            eventPrice = eventPrice,
                                            onSale = true
                                        )
                                    )
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            readProductsCallback?.onReadProducts(list)
                        }
                    }
                })
            }
        }
    }
}

