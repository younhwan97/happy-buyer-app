package kr.co.younhwan.happybuyer.data.source.product

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ProductRemoteDataSource : ProductSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/basket" // 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun readProducts(
        selectedCategory: String,
        sort: String,
        keyword: String?,
        readProductsCallback: ProductSource.ReadProductsCallback?
    ) {
        runBlocking {
            val list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                list.addAll(reads(selectedCategory, sort, keyword))
            }

            job.join()
            readProductsCallback?.onReadProducts(list)
        }
    }

    override fun readProduct(
        productId: Int,
        kakaoAccountId: Long,
        readProductCallback: ProductSource.ReadProductCallback?
    ) {
        runBlocking {
            var product: ProductItem? = null

            val job = GlobalScope.launch {
                product = read(productId, kakaoAccountId)
            }

            job.join()
            readProductCallback?.onReadProduct(product)
        }
    }
}

suspend fun reads(
    selectedCategory: String,
    sort: String,
    keyword: String?
): ArrayList<ProductItem> {
    val list = ArrayList<ProductItem>()

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    var site =
        "http://happybuyer.co.kr/products/api/app/reads?category=${selectedCategory}&sort=${sort}"

    if (!keyword.isNullOrEmpty()) {
        site += "&keyword=${keyword}"
    }

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
                    val onSale = if (obj.isNull("on_sale")) false else obj.getBoolean("on_sale")
                    val eventPrice = if (obj.isNull("event_price")) 0 else obj.getInt("event_price")
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

    return list
}

suspend fun read(productId: Int, kakaoAccountId: Long): ProductItem? {
    var product: ProductItem? = null

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/products/api/app/read?pid=${productId}&uid=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)

        val success = json.getBoolean("success")

        if (success) {
            val obj = json.getJSONObject("data")

            val productStatus = obj.getString("status")
            val productCategory = obj.getString("category")

            if (productStatus == "판매중") {
                val productId = obj.getInt("product_id")
                val productName = obj.getString("name")
                val productPrice = obj.getInt("price")
                val productImage = obj.getString("image_url")
                val onSale = if (obj.isNull("on_sale")) false else obj.getBoolean("on_sale")
                val eventPrice = if (obj.isNull("event_price")) 0 else obj.getInt("event_price")
                val sales = if (obj.isNull("sales")) 0 else obj.getInt("sales")
                val isWished = if (obj.isNull("is_wished")) false else obj.getBoolean("is_wished")

                product = ProductItem(
                    productId = productId,
                    productName = productName,
                    productPrice = productPrice,
                    productImageUrl = productImage,
                    onSale = onSale,
                    eventPrice = eventPrice,
                    sales = sales,
                    isWished = isWished
                )
            }
        }
    }

    return product
}



