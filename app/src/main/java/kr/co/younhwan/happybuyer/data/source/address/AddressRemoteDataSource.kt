package kr.co.younhwan.happybuyer.data.source.address

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object AddressRemoteDataSource : AddressSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://192.168.0.11/address/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    // CREATE
    override fun create(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        createCallback: AddressSource.CreateCallback?
    ) {
        runBlocking {
            var addressId = -1

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = serverInfo

                // 새로운 데이터 생성, 데이터 수정을 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("receiver_name", addressItem.addressReceiver)
                jsonData.put("phone_number", addressItem.addressPhone)
                jsonData.put("address", addressItem.address)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val resultText = response.body?.string()!!.trim()
                    val json = JSONObject(resultText)
                    val success = json.getBoolean("success")
                    if (success) {
                        addressId = json.getInt("address_id")
                    }
                }
            }

            job.join()
            createCallback?.onCreate(addressId)
        }
    }

    // READ
    override fun read(kakaoAccountId: Long, readCallback: AddressSource.ReadCallback?) {
        runBlocking {
            val list = ArrayList<AddressItem>()

            val job = GlobalScope.launch {
                // API 서버 주소
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 읽어오기 위한 GET Request 생성
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
                            val userId = obj.getLong("user_id")

                            if (userId == kakaoAccountId) {
                                val addressId = obj.getInt("address_id")
                                val receiver = obj.getString("receiver")
                                val phone = obj.getString("phone")
                                val address = obj.getString("phone")

                                list.add(
                                    AddressItem(
                                        addressId = addressId,
                                        addressReceiver = receiver,
                                        addressPhone = phone,
                                        address = address
                                    )
                                )
                            }
                        }
                    }

                }
            }

            job.join()
            readCallback?.onRead(list)
        }
    }
}