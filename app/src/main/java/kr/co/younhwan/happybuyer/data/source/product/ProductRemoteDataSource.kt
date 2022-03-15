package kr.co.younhwan.happybuyer.data.source.product

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ProductRemoteDataSource : ProductSource {

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

    /***********************************************************************/
    /******************************* Event *******************************/

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
    /***********************************************************************/
    /***********************************************************************/


    /***********************************************************************/
    /******************************* Wished *******************************/

    override fun createProductInWished(
        kakaoAccountId: Long,
        productId: Int,
        createProductInWishedCallback: ProductSource.CreateProductInWishedCallback?
    ) {
        // deleteProductInWished 기능도 수행
        runBlocking {
            var perform: String? = null

            val job = GlobalScope.launch {
                perform = createInWished(kakaoAccountId, productId)
                // perform -> create : 상품이 찜 목록에 추가
                // perform -> delete : 상품이 찜 목록에서 제거
            }

            job.join()
            createProductInWishedCallback?.onCreateProductInWished(perform)
        }
    }

    override fun readWishedProductsId(
        kakaoAccountId: Long,
        readWishedProductsIdCallback: ProductSource.ReadWishedProductsIdCallback?
    ) {
        runBlocking {
            val list = ArrayList<Int>()

            val job = GlobalScope.launch {
                list.addAll(readWished(kakaoAccountId))
            }

            job.join()
            readWishedProductsIdCallback?.onReadWishedProductsId(list)
        }
    }
    /***********************************************************************/
    /***********************************************************************/

    /***********************************************************************/
    /******************************* Basket *******************************/
    // CREATE
    override fun createProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductInBasketCallback: ProductSource.CreateProductInBasketCallback?
    ) {
        runBlocking {
            var resultCount = 0

            val job = GlobalScope.launch {
                resultCount = createInBasket(kakaoAccountId, productId, count)
            }

            job.join()
            createProductInBasketCallback?.onCreateProductInBasket(resultCount)
        }
    }

    // READ
    override fun readProductsInBasket(
        kakaoAccountId: Long,
        readProductsInBasketCallback: ProductSource.ReadProductsInBasketCallback?
    ) {
        runBlocking {
            val list = ArrayList<BasketItem>()

            val job = GlobalScope.launch {
                list.addAll(readInBasket(kakaoAccountId))
            }

            job.join()
            readProductsInBasketCallback?.onReadProductsInBasket(list)
        }
    }

    // UPDATE (MINUS)
    override fun minusProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        minusProductInBasketCallback: ProductSource.MinusProductInBasketCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = minusInBasket(kakaoAccountId, productId)
            }

            job.join()
            minusProductInBasketCallback?.onMinusProductInBasket(isSuccess)
        }
    }

    // DELETE
    override fun deleteProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductInBasketCallback: ProductSource.DeleteProductInBasketCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = deleteInBasket(kakaoAccountId, productId)
            }

            job.join()
            deleteProductInBasketCallback?.onDeleteProductInBasket(isSuccess)
        }
    }
    /***********************************************************************/
    /***********************************************************************/
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


/***********************************************************************/
/******************************* Event *******************************/

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

        val success = json.getBoolean("success")
        val data = JSONArray(json["data"].toString())

        if (success) { // 이벤트 상품이 있는 경우
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
    }

    // 이벤트 상품이 없는 경우 빈 리스트를 반환
    return list
}

/***********************************************************************/
/******************************* Wished *******************************/

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
        val success = json.getBoolean("success")
        val perform = json.getString("perform")

        if (success)
            return perform
    }

    // 에러
    return "error"
}

suspend fun readWished(kakaoAccountId: Long): ArrayList<Int> {
    val list = ArrayList<Int>()

    if (kakaoAccountId == -1L) return list

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

        val success = json.getBoolean("success")
        if (success) { // 유저가 찜한 상품이 있을 때
            val data = JSONArray(json["data"].toString())
            for (i in 0 until data.length()) {
                val obj = data.getJSONObject(i)
                val userId = obj.getLong("user_id")

                if (kakaoAccountId == userId) {
                    list.add(obj.getInt("product_id"))
                }
            }
        }
    }

    // 쿼리 전달이 잘못되었거나 유저가 찜한 상품이 없을 때
    return list
}

/***********************************************************************/
/******************************* Basket *******************************/

suspend fun createInBasket(kakaoAccountId: Long, productId: Int, count: Int): Int {
    var resultCount = 0

    // 클라이언트 생성
    val client = OkHttpClient()

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

    return resultCount
}

suspend fun readInBasket(kakaoAccountId: Long): ArrayList<BasketItem> {
    val list = ArrayList<BasketItem>()

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

    return list
}

suspend fun minusInBasket(kakaoAccountId: Long, productId: Int): Boolean {
    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/basket/api/app/update?pid=${productId}&uid=${kakaoAccountId}&perform=minus"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()
    var success = false

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        success = json.getBoolean("success")
    }

    return success
}

suspend fun deleteInBasket(kakaoAccountId: Long, productId: Int): Boolean {
    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/basket/api/app/delete?pid=${productId}&uid=${kakaoAccountId}"
    val request = Request.Builder().url(site).get().build()

    // 응답
    val response = client.newCall(request).execute()
    var success = false

    if (response.isSuccessful) {
        val resultText = response.body?.string()!!.trim()
        val json = JSONObject(resultText)
        success = json.getBoolean("success")
    }

    return success
}

