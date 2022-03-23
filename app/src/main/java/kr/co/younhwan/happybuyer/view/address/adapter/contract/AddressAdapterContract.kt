package kr.co.younhwan.happybuyer.view.address.adapter.contract

import kr.co.younhwan.happybuyer.data.AddressItem

interface AddressAdapterContract {
    interface View {

        fun notifyAdapter()

        var onClickFunOfEditBtn: ((AddressItem) -> Unit)?
    }

    interface Model {

        fun addItems(addressItems: ArrayList<AddressItem>)

        fun setSelectMode(canBeSelected: Boolean)

    }
}