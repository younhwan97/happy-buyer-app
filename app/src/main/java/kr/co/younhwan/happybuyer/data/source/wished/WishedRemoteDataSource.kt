package kr.co.younhwan.happybuyer.data.source.wished

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object WishedRemoteDataSource : WishedSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/wished/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    // CREATE or DELETE
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

                    perform = if (success) {
                        resultPerform
                    } else {
                        "error"
                    }
                }
            }

            job.join()
            createOrDeleteProductCallback?.onCreateOrDeleteProduct(perform)
        }
    }

    // READ
    override fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: WishedSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<ProductItem>()

            launch {
                // 요청
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 읽기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
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
                                val productCategory = obj.getString("category")

                                if (productStatus == "판매중") {
                                    val productId = obj.getInt("product_id")
                                    val productName = obj.getString("name")
                                    val productPrice = obj.getInt("price")
                                    val productImage = obj.getString("image_url")
                                    val onSale =
                                        if (obj.isNull("on_sale")) false else obj.getBoolean("on_sale")
                                    val eventPrice =
                                        if (obj.isNull("event_price")) 0 else obj.getInt("event_price")
                                    val sales = if (obj.isNull("sales")) 0 else obj.getInt("sales")

                                    list.add(
                                        ProductItem(
                                            productId = productId,
                                            productName = productName,
                                            productPrice = productPrice,
                                            productImageUrl = productImage,
                                            onSale = onSale,
                                            eventPrice = eventPrice,
                                            sales = sales
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