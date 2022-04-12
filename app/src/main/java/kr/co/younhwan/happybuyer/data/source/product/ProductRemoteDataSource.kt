package kr.co.younhwan.happybuyer.data.source.product

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object ProductRemoteDataSource : ProductSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://192.168.0.11/products/api" // API 서버

    override fun readProducts(
        selectedCategory: String,
        sortBy: String?,
        page: Int,
        keyword: String?,
        readProductsCallback: ProductSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<ProductItem>()

            launch {
                // API 서버 주소
                var site = "${serverInfo}?category=${selectedCategory}&page=${page}"

                if (!sortBy.isNullOrEmpty()) {
                    site += "&sort=${sortBy}"
                }

                if (!keyword.isNullOrEmpty()) {
                    site += "&keyword=${keyword}"
                }

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

                                if (productStatus == "판매중" && (selectedCategory == "total" || productCategory == selectedCategory)) {
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