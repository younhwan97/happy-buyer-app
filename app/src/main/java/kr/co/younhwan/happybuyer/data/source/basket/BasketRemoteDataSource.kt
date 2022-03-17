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
    override fun createProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        count: Int,
        createProductInBasketCallback: BasketSource.CreateProductInBasketCallback?
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

    override fun readProductsInBasket(
        kakaoAccountId: Long,
        readProductsInBasketCallback: BasketSource.ReadProductsInBasketCallback?
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

    override fun updateProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        perform: String,
        updateProductInBasketCallback: BasketSource.UpdateProductInBasketCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                isSuccess = updateInBasket(kakaoAccountId, productId, perform)
            }

            job.join()
            updateProductInBasketCallback?.onUpdateProductInBasket(isSuccess)
        }
    }

    override fun deleteProductInBasket(
        kakaoAccountId: Long,
        productId: Int,
        deleteProductInBasketCallback: BasketSource.DeleteProductInBasketCallback?
    ) {
        runBlocking {
            var isSuccess = false

            val job = GlobalScope.launch {
                // 클라이언트 생성
                val client = OkHttpClient()

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
            deleteProductInBasketCallback?.onDeleteProductInBasket(isSuccess)
        }
    }
}

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

suspend fun updateInBasket(kakaoAccountId: Long, productId: Int, perform: String): Boolean {
    // 클라이언트 생성
    val client = OkHttpClient()

    // 요청
    val site =
        "http://happybuyer.co.kr/basket/api/app/update?pid=${productId}&uid=${kakaoAccountId}&perform=${perform}"
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

suspend fun deleteProduct(kakaoAccountId: Long, productId: Int): Boolean {
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

suspend fun deleteProducts(){

}

