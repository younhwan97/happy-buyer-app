package kr.co.younhwan.happybuyer.view.address

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource
import kr.co.younhwan.happybuyer.view.address.adapter.contract.AddressAdapterContract

class AddressPresenter(
    private val view: AddressContract.View,
    private val addressData: AddressRepository,
    private val addressAdapterModel: AddressAdapterContract.Model,
    private val addressAdapterView: AddressAdapterContract.View
) : AddressContract.Model {

    init {
        addressAdapterView.onClickFunOfEditBtn = {
            onClickListenerOfEditBtn(it)
        }

        addressAdapterView.onClickFunOfSelectBtn = {
            onClickListenerOfSelectBtn(it)
        }
    }

    private val app = view.getAct().application as GlobalApplication

    private fun onClickListenerOfEditBtn(addressItem: AddressItem) {
        view.createAddAddressAct(addressItem)
    }

    private fun onClickListenerOfSelectBtn(addressItem: AddressItem) {
        view.finishAct(addressItem)
    }

    override fun loadAddress(isSelectMode: Boolean) {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {
                        // 기본 배송지가 제일 상단에 위치하도록 변경
                        val sortedList = ArrayList<AddressItem>()
                        var defaultAddressIndex = -1

                        for (index in 0 until list.size) {
                            if (list[index].isDefault == true) {
                                defaultAddressIndex = index
                                sortedList.add(list[index])
                                break
                            }
                        }

                        if (defaultAddressIndex != -1) {
                            for (index in 0 until list.size) {
                                if (index != defaultAddressIndex) {
                                    sortedList.add(list[index])
                                }
                            }
                        }

                        view.loadAddressCallback(sortedList.size)
                        addressAdapterModel.setSelectMode(isSelectMode)
                        addressAdapterModel.addItems(sortedList)
                        addressAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            view.loadAddressCallback(0)
        }
    }
}