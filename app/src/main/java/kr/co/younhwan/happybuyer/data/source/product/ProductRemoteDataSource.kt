package kr.co.younhwan.happybuyer.data.source.product

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.CategoryItem
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
            var list = ArrayList<CategoryItem>()
            val job = GlobalScope.launch {
                list = getItem(selectedCategory)
            }

            job.join()
            loadImageCallback?.onLoadImages(list)
        }
    }
}

suspend fun getItem(selectedCategory: String): ArrayList<CategoryItem> {
    val list = ArrayList<CategoryItem>()

    // 클라이언트 만들기
    val client = OkHttpClient()

    // 요청
    val site = "http://192.168.0.11/products/api/app/read?category=${selectedCategory}"
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
            var productName: String
            var productPrice: Int
            var productImage: String
            if (productStatus == "판매중" && productCategory == selectedCategory) {
                productName = obj.getString("name")
                productPrice = obj.getInt("price")
                productImage = obj.getString("image_url")
                list.add(CategoryItem(productImage, productName, productPrice))
            } else { // 판매중인 상품이 아니거나 잘못된 카테고리의 상품
                continue
            }
        }
    }

    return list
}