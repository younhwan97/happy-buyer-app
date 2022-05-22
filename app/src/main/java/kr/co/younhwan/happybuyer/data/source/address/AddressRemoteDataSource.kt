package kr.co.younhwan.happybuyer.data.source.address

import kotlinx.coroutines.*
import kr.co.younhwan.happybuyer.data.AddressItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object AddressRemoteDataSource : AddressSource {
    private val client = OkHttpClient() // 클라이언트
    private const val serverInfo = "http://happybuyer.co.kr/address/api" // API 서버
    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    // CREATE
    override fun create(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        createCallback: AddressSource.CreateCallback?
    ) {
        runBlocking {
            var addressId = -1

            launch {
                // API 서버 주소
                val site = serverInfo

                // 새로운 데이터 생성, 데이터 수정을 위한 POST Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("receiver_name", addressItem.addressReceiver)
                jsonData.put("phone_number", addressItem.addressPhone)
                jsonData.put("address", addressItem.address)
                jsonData.put("is_default", addressItem.isDefault)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).post(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            createCallback?.onCreate(addressId)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        val success = json.getBoolean("success")
                        if (success) {
                            addressId = json.getInt("address_id")
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            createCallback?.onCreate(addressId)
                        }
                    }
                })
            }
        }
    }

    // READ
    override fun read(
        kakaoAccountId: Long,
        readCallback: AddressSource.ReadCallback?
    ) {
        runBlocking {
            val list = ArrayList<AddressItem>()

            launch {
                // API 서버 주소
                val site = "${serverInfo}?id=${kakaoAccountId}"

                // 데이터를 읽어오기 위한 GET Request 생성
                val request = Request.Builder().url(site).get().build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            readCallback?.onRead(list)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
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
                                    val address = obj.getString("address")
                                    val isDefaultAddress = obj.getInt("is_default") != 0

                                    list.add(
                                        AddressItem(
                                            addressId = addressId,
                                            addressReceiver = receiver,
                                            addressPhone = phone,
                                            address = address,
                                            isDefault = isDefaultAddress
                                        )
                                    )
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            readCallback?.onRead(list)
                        }
                    }
                })
            }
        }
    }

    // UPDATE
    override fun update(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        updateCallback: AddressSource.UpdateCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터를 수정하기 위한 PUT Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("address_id", addressItem.addressId)
                jsonData.put("receiver_name", addressItem.addressReceiver)
                jsonData.put("phone_number", addressItem.addressPhone)
                jsonData.put("address", addressItem.address)
                jsonData.put("is_default", addressItem.isDefault)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).put(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            updateCallback?.onUpdate(isSuccess)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        isSuccess = json.getBoolean("success")

                        CoroutineScope(Dispatchers.Main).launch {
                            updateCallback?.onUpdate(isSuccess)
                        }
                    }
                })
            }
        }
    }

    // DELETE
    override fun delete(
        kakaoAccountId: Long,
        addressId: Int,
        deleteCallback: AddressSource.DeleteCallback?
    ) {
        runBlocking {
            var isSuccess = false

            launch {
                // API 서버 주소
                val site = serverInfo

                // 데이터를 삭제하기 위한 DELETE Request 생성
                val jsonData = JSONObject()
                jsonData.put("user_id", kakaoAccountId)
                jsonData.put("address_id", addressId)
                val requestBody = jsonData.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(site).delete(requestBody).build()

                // 응답
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        CoroutineScope(Dispatchers.Main).launch {
                            deleteCallback?.onDelete(isSuccess)
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val resultText = response.body?.string()!!.trim()
                        val json = JSONObject(resultText)
                        isSuccess = json.getBoolean("success")

                        CoroutineScope(Dispatchers.Main).launch {
                            deleteCallback?.onDelete(isSuccess)
                        }
                    }
                })
            }
        }
    }
}