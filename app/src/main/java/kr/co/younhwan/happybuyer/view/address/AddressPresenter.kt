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

    val app = view.getAct().application as GlobalApplication

    override fun loadAddress(isSelectMode: Boolean) {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {
                        view.loadAddressCallback(list.size)
                        addressAdapterModel.setSelectMode(isSelectMode)
                        addressAdapterModel.addItems(list)
                        addressAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            view.loadAddressCallback(0)
            addressAdapterModel.setSelectMode(false)
            addressAdapterModel.addItems(ArrayList<AddressItem>())
            addressAdapterView.notifyAdapter()
        }
    }

    private fun onClickListenerOfEditBtn(addressItem: AddressItem) = view.createAddAddressAct(addressItem)

    private fun onClickListenerOfSelectBtn(addressItem: AddressItem) = view.finishAddressAct(addressItem)
}