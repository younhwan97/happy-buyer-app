package kr.co.younhwan.happybuyer.view.addeditaddress

import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource

class AddAddressPresenter(
    private val view: AddAddressContract.View,
    private val addressData: AddressRepository
) : AddAddressContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun checkHasDefaultAddress() {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {
                        var hasDefaultAddress = false

                        if (list.size != 0) {
                            for (item in list) {
                                if (item.isDefault == true) {
                                    hasDefaultAddress = true
                                    break
                                }
                            }
                        }

                        view.checkHasDefaultAddressCallback(hasDefaultAddress)
                    }
                }
            )
        }
    }

    override fun addAddress(addressItem: AddressItem) {
        if (app.isLogined) {
            // address id(primary key)에 따라 주소를 새롭게 추가하거나 수정
            if (addressItem.addressId == -1) {
                // 주소를 새롭게 추가하는 경우
                addressData.create(
                    kakaoAccountId = app.kakaoAccountId,
                    addressItem = addressItem,
                    createCallback = object : AddressSource.CreateCallback {
                        override fun onCreate(addressId: Int) {
                            if (addressId == -1) {
                                addressItem.addressId = addressId
                                view.addAddressCallback(addressItem, false)
                            } else {
                                addressItem.addressId = addressId
                                view.addAddressCallback(addressItem, true)
                            }
                        }
                    }
                )
            } else {
                // 기존의 주소를 수정하는 경우
                addressData.update(
                    kakaoAccountId = app.kakaoAccountId,
                    addressItem = addressItem,
                    updateCallback = object : AddressSource.UpdateCallback {
                        override fun onUpdate(isSuccess: Boolean) {
                            view.addAddressCallback(addressItem, isSuccess)
                        }
                    }
                )
            }
        }
    }

    override fun deleteAddress(addressId: Int) {
        if (app.isLogined) {
            if (addressId != -1) {
                addressData.delete(
                    kakaoAccountId = app.kakaoAccountId,
                    addressId = addressId,
                    deleteCallback = object : AddressSource.DeleteCallback{
                        override fun onDelete(isSuccess: Boolean) {
                            view.deleteAddressCallback(isSuccess)
                        }
                    }
                )
            }
        }
    }
}