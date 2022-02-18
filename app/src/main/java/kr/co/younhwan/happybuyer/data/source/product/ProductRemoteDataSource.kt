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

    override fun getImages(
        context: Context,
        selectedCategory: String,
        loadImageCallback: ProductSource.LoadImageCallback?
    ) {

        runBlocking {
            var list = ArrayList<ProductItem>()
            val job = GlobalScope.launch {
                list = getItem(selectedCategory)
            }

            job.join()
            loadImageCallback?.onLoadImages(list)
        }
    }

    override fun addProductToBasket(productId: Int) {

        runBlocking {
            val job = GlobalScope.launch {
                addProduct(productId)
            }

            job.join()
        }

    }

    override fun addProductToWished(productId: Int) {
        TODO("Not yet implemented")
    }
}

suspend fun getItem(selectedCategory: String): ArrayList<ProductItem> {
    val list = ArrayList<ProductItem>()

    // 클라이언트 만들기
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

                list.add(ProductItem(productId, productImage, productName, productPrice))
            } else { // 판매중인 상품이 아니거나 잘못된 카테고리의 상품
                continue
            }
        }
    }

    return list
}

suspend fun addProduct(productId: Int): Unit {

    val client = OkHttpClient()


}