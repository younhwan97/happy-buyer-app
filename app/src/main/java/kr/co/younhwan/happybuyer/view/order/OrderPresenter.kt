package kr.co.younhwan.happybuyer.view.order

import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource
import kr.co.younhwan.happybuyer.view.order.adapter.contract.OrderAdapterContract

class OrderPresenter(
    private val view: OrderContract.View,
    private val addressData: AddressRepository,
    private val orderAdapterModel: OrderAdapterContract.Model,
    private val orderAdapterView: OrderAdapterContract.View
) : OrderContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun loadDefaultAddress() {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {
                        var defaultAddressItem: AddressItem? = null

                        for (item in list) {
                            if (item.isDefault == true) {
                                defaultAddressItem = item
                                break
                            }
                        }

                        view.loadDefaultAddressCallback(defaultAddressItem)
                    }
                }
            )
        }
    }

    override fun setOrderProduct(list: ArrayList<BasketItem>) {
        if (app.isLogined) {
            orderAdapterModel.addItems(list)
        }
    }
}