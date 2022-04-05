package kr.co.younhwan.happybuyer.data.source.basket

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.BasketItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object BasketRemoteDataSource : BasketSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/basket/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    // CREATE
    override fun createOrUpdateProduct(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createOrUpdateProductCallback: BasketSource.CreateOrUpdateProductCallback?
    ) {
        runBlocking {
            var resultCount = 0

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 새로운 데이터 생성을 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("product_id", productId)
                jsonData.put("count", count)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    val success = json.getBoolean("success")

                    if (success) {
                        resultCount = json.getInt("result_count")
                    }
                }
            }

            job.join()
            createOrUpdateProductCallback?.onCreateOrUpdateProduct(resultCount)
        }
    }

    // READ
    override fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: BasketSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<BasketItem>()

            launch {
                // API 서버 주소
                val site = "${serverInfo}?id=${kakaoAccountId}"

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

                                if (productStatus == "판매중" || productStatus == "품절") {
                                    val productId = obj.getInt("product_id")
                                    val productName = obj.getString("name")
                                    val productPrice = obj.getInt("price")
                                    val productImage = obj.getString("image_url")
                                    val countInBasket = obj.getInt("count_in_basket")
                                    val onSale =
                                        if (obj.isNull("on_sale")) false else obj.getBoolean("on_sale")
                                    val eventPrice =
                                        if (obj.isNull("event_price")) 0 else obj.getInt("event_price")

                                    list.add(
                                        BasketItem(
                                            isChecked = true,
                                            productStatus = productStatus,
                                            productId = productId,
                                            productName = productName,
                                            productPrice = productPrice,
                                            productImageUrl = productImage,
                                            countInBasket = countInBasket,
                                            onSale = onSale,
                                            eventPrice = eventPrice
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

    // UPDATE
    override fun updateProduct(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductCallback: BasketSource.UpdateProductCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터 수정을 위한 PUT Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("product_id", productId)
                jsonData.put("perform", perform)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).put(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    isSuccess = json.getBoolean("success")
                }
            }

            job.join()
            updateProductCallback?.onUpdateProduct(isSuccess)
        }
    }

    // DELETE
    override fun deleteProducts(
        kakaoAccountId: Long,
        productId: ArrayList<Int>,
        deleteProductsCallback: BasketSource.DeleteProductsCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터 삭제를 위한 DELETE Request 생성
                val jsonData = JSONArray()
                for (id in productId) {
                    val jsonTemp = JSONObject()
                    jsonTemp.put("user_id", kakaoAccountId)
                    jsonTemp.put("product_id", id)
                    jsonData.put(jsonTemp)
                }
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).delete(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    isSuccess = json.getBoolean("success")
                }
            }

            job.join()
            deleteProductsCallback?.onDeleteProducts(isSuccess)
        }
    }
}