package kr.co.younhwan.happybuyer.data.source.basket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.BasketItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object BasketRemoteDataSource : BasketSource {
    private val client = OkHttpClient() // 클라이언트

    override fun createProduct(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductCallback: BasketSource.CreateProductCallback?
    ) {
        runBlocking {
            var resultCount = 0

            val job = GlobalScope.launch {
                // 요청
                val site =
                    "http://happybuyer.co.kr/basket/api/app/create?pid=${productId}&uid=${kakaoAccountId}&count=${count}"
                val request = Request.Builder().url(site).get().build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    val success = json.getBoolean("success")

                    if (success)
                        resultCount = json.getInt("result_count")
                }
            }

            job.join()
            createProductCallback?.onCreateProduct(resultCount)
        }
    }

    override fun readProducts(
        kakaoAccountId: Long,
        readProductsCallback: BasketSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<BasketItem>()

            val job = GlobalScope.launch {
                // 요청
                val site = "http://happybuyer.co.kr/basket/api/app/read?id=${kakaoAccountId}"
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
                            // val productCategory = obj.getString("category")

                            if (productStatus == "판매중" || productStatus == "품절") {
                                val productId = obj.getInt("product_id")
                                val productName = obj.getString("name")
                                val productPrice = obj.getInt("price")
                                val productImage = obj.getString("image_url")
                                val countInBasket = obj.getInt("count_in_basket")
                                val onSale = if (obj.isNull("on_sale")) false else obj.getBoolean("on_sale")
                                val eventPrice = if (obj.isNull("event_price")) 0 else obj.getInt("event_price")

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
                }
            }

            job.join()
            readProductsCallback?.onReadProducts(list)
        }
    }

    override fun updateProduct(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductCallback: BasketSource.UpdateProductCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // 요청
                val site =
                    "http://happybuyer.co.kr/basket/api/app/update?pid=${productId}&uid=${kakaoAccountId}&perform=${perform}"
                val request = Request.Builder().url(site).get().build()

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

    override fun deleteProduct(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductCallback: BasketSource.DeleteProductCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // 요청
                val site =
                    "http://happybuyer.co.kr/basket/api/app/delete?pid=${productId}&uid=${kakaoAccountId}"
                val request = Request.Builder().url(site).get().build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    isSuccess = json.getBoolean("success")
                }
            }

            job.join()
            deleteProductCallback?.onDeleteProduct(isSuccess)
        }
    }
}