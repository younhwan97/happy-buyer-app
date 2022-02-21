package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.ProductItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

object ProductRemoteDataSource : ProductSource {

    override fun getProducts(
        context: Context,
        selectedCategory: String,
        kakaoAccountId: Long?,
        loadImageCallback: ProductSource.LoadProductCallback?
    ) {

        runBlocking {

            var list = ArrayList<ProductItem>()

            val job = GlobalScope.launch {
                val wishedList = getWishedProduct(kakaoAccountId)
                list = getItem(selectedCategory, wishedList)
            }

            job.join()
            loadImageCallback?.onLoadProducts(list)
        }
    }

    override fun addProductToBasket(
        kakaoAccountId: Long,
        productId: Int,
        addProductCallback: ProductSource.AddProductCallback?
    ) {

        runBlocking {
            var success = false
            val job = GlobalScope.launch {
                success = createBasket(kakaoAccountId, productId)
            }

            job.join()
            addProductCallback?.onAddProduct(success)
        }

    }

    override fun addProductToWished(
        kakaoAccountId: Long,
        productId: Int,
        addProductToWishedCallback: ProductSource.AddProductToWishedCallback?
    ) {
        runBlocking {
            var explain : String? = null
            val job = GlobalScope.launch {
                explain = createWished(kakaoAccountId, productId)
            }

            job.join()
            addProductToWishedCallback?.onAddProductToWished(explain)
        }
    }
}

suspend fun getWishedProduct(kakaoAccountId: Long?): ArrayList<Int>{
    val list = ArrayList<Int>()

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
            if (kakaoAccountId == userId) {
                val productId = obj.getInt("product_id")
                list.add(productId)
            }
        }
    }

    return list
}


suspend fun getItem(selectedCategory: String, wishedList: ArrayList<Int>): ArrayList<ProductItem> {
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
            val productCategory = obj.getString("category")

            if (productStatus == "판매중" && productCategory == selectedCategory) {
                val productId = obj.getInt("product_id")
                val productName = obj.getString("name")
                val productPrice = obj.getInt("price")
                val productImage = obj.getString("image_url")
                var isWished = false

                for (item in wishedList){
                    if(item == productId){
                        isWished = true
                    }
                }

                list.add(ProductItem(productId, productImage, productName, productPrice, isWished))
            } else { // 판매중인 상품이 아니거나 잘못된 카테고리의 상품
                continue
            }
        }
    }

    return list
}

suspend fun createBasket(kakaoAccountId: Long, productId: Int): Boolean {

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

suspend fun createWished(kakaoAccountId: Long, productId: Int): String?{
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

    return null
}