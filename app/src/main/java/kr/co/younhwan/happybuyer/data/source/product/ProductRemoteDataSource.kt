package kr.co.younhwan.happybuyer.data.source.product

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ProductRemoteDataSource : ProductSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/products/api" // API 서버

    override fun readProducts(
        selectedCategory: String,
        keyword: String?,
        readProductsCallback: ProductSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                // API 서버 주소
                var site = "${serverInfo}?category=${selectedCategory}"

                if (!keyword.isNullOrEmpty()) {
                    site += "&keyword=${keyword}"
                }

                // 데이터를 읽기 위한 GET Request 생성
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
                }
            }

            job.join()
            readProductsCallback?.onReadProducts(list)
        }
    }
}