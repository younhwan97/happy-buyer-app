package kr.co.younhwan.happybuyer.view.addeditaddress

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource

class AddAddressPresenter(
    private val view: AddAddressContract.View,
    private val addressData: AddressRepository
) : AddAddressContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun addAddress(addressItem: AddressItem) {
        if (app.isLogined) {
            addressData.create(
                kakaoAccountId = app.kakaoAccountId,
                addressItem = addressItem,
                object : AddressSource.CreateCallback {
                    override fun onCreate(addressId: Int) {
                        view.addAddressCallback(addressId, addressItem)
                    }
                }
            )
        }
    }
}