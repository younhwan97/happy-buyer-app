package kr.co.younhwan.happybuyer.view.address

import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource

class AddressPresenter(
    private val view: AddressContract.View,
    private val addressData: AddressRepository
) : AddressContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun loadAddress() {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {

                    }
                }
            )
        }
    }
}