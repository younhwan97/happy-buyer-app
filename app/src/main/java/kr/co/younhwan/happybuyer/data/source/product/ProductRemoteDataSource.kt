package kr.co.younhwan.happybuyer.data.source.product

import android.util.Log
import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ProductRemoteDataSource : ProductSource {

    override fun createProductInWished(
        kakaoAccountId: Long,
        productId: Int,
        createProductInWishedCallback: ProductSource.CreateProductInWishedCallback?
    ) {
        // deleteProductInWished 기능도 수행
        runBlocking {
            var explain: String? = null

            val job = GlobalScope.launch {
                explain = createInWished(kakaoAccountId, productId)
                // explain -> create : 상품이 찜 목록에 추가
                // explain -> delete : 상품이 찜 목록에서 제거
            }

            job.join()
            createProductInWishedCallback?.onCreateProductInWished(explain)
        }
    }

    override fun createProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        createProductInBasketCallback: ProductSource.CreateProductInBasketCallback?
    ) {

        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = createInBasket(kakaoAccountId, productId)
            }

            job.join()
            createProductInBasketCallback?.onCreateProductInBasket(isSuccess)
        }

    }

    override fun readProducts(
        kakaoAccountId: Long?,
        selectedCategory: String,
        readProductCallback: ProductSource.ReadProductsCallback?
    ) {

        runBlocking {

            var list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                val wishedIdList = readWishedProductIdList(kakaoAccountId)
                list = read(selectedCategory, wishedIdList)
            }

            job.join()
            readProductCallback?.onReadProducts(list)
        }
    }


    override fun readProductsInBasket(
        kakaoAccountId: Long,
        readProductsInBasketCallback: ProductSource.ReadProductsInBasketCallback?
    ) {

        runBlocking {

            var list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                val basketIdAndCountList = readBasketProductIdAndCountList(kakaoAccountId)
                list = readBasket(basketIdAndCountList)
            }

            job.join()
            readProductsInBasketCallback?.onReadProductsInBasket(list)
        }
    }

    override fun readProductsInBasketCount(
        kakaoAccountId: Long,
        readProductsInBasketCountCallback: ProductSource.ReadProductsInBasketCountCallback?
    ) {
        runBlocking {

            var count = 0

            val job = GlobalScope.launch {
                val basketIdAndCountList = readBasketProductIdAndCountList(kakaoAccountId)
                for (item in basketIdAndCountList) {
                    count += item["productCount"]!!
                }
            }

            job.join()
            readProductsInBasketCountCallback?.onReadProductsInBasketCount(count)
        }
    }

    override fun readEventProducts(
        readEventProductsCallback: ProductSource.ReadEventProductsCallback?
    ) {
        runBlocking {
            var list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                list = readEvent()
            }

            job.join()
            readEventProductsCallback?.onReadEventProduct(list)
        }
    }
}

suspend fun createInWished(kakaoAccountId: Long, productId: Int): String? {

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/wished/api/app/create?pid=${productId}&uid=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        return json.getString("explain")
    }

    // DB 에러
    return null
}

suspend fun createInBasket(kakaoAccountId: Long, productId: Int): Boolean {

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/basket/api/app/create?pid=${productId}&uid=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return true
    }

    return false
}


suspend fun read(selectedCategory: String, wishedIdList: ArrayList<Int>): ArrayList<ProductItem> {
    val list = ArrayList<ProductItem>()

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site = "http://happybuyer.co.kr/products/api/app/read?category=${selectedCategory}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
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
                var isWished = false

                for (id in wishedIdList) {
                    if (id == productId) {
                        isWished = true
                    }
                }

                list.add(ProductItem(productId, productImage, productName, productPrice, isWished))
            }
        }
    }

    return list
}

suspend fun readBasket(basketIdAndCountList: ArrayList<Map<String, Int>>): ArrayList<ProductItem> {
    val list = ArrayList<ProductItem>()
    val selectedCategory = "total"

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site = "http://happybuyer.co.kr/products/api/app/read?category=${selectedCategory}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
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
                val isWished = false

                for (item in basketIdAndCountList) {
                    if (item["productId"] == productId) {
                        list.add(
                            ProductItem(
                                productId,
                                productImage,
                                productName,
                                productPrice,
                                isWished,
                                item["productCount"]!!
                            )
                        )
                    }
                }
            }
        }
    }

    return list
}

suspend fun readWishedProductIdList(kakaoAccountId: Long?): ArrayList<Int> {
    val list = ArrayList<Int>()

    if (kakaoAccountId == null || kakaoAccountId == -1L) return list

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site = "http://happybuyer.co.kr/wished/api/app/read?id=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        val data = JSONArray(json["data"].toString())

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val userId = obj.getLong("user_id")

            if (kakaoAccountId == userId)
                list.add(obj.getInt("product_id"))
        }
    }

    return list
}

suspend fun readBasketProductIdAndCountList(kakaoAccountId: Long): ArrayList<Map<String, Int>> {
    val list = ArrayList<Map<String, Int>>()

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site = "http://happybuyer.co.kr/basket/api/app/read?id=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        val data = JSONArray(json["data"].toString())

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val map = mutableMapOf<String, Int>()
            map["productId"] = obj.getInt("product_id")
            map["productCount"] = obj.getInt("count")
            list.add(map)
        }
    }

    return list
}

suspend fun readEvent(): ArrayList<ProductItem> {
    val list = ArrayList<ProductItem>()

    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site = "http://happybuyer.co.kr/event/api/app/read"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
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
                var isWished = false

                Log.d("test", obj.toString())

                list.add(
                    ProductItem(
                        productId,
                        productImage,
                        productName,
                        productPrice,
                        isWished,
                        eventPrice = eventPrice
                    )
                )
            }
        }
    }
    return list
}


