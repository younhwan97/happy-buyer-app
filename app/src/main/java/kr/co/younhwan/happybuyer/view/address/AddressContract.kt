package kr.co.younhwan.happybuyer.view.address

import kr.co.younhwan.happybuyer.data.AddressItem

interface AddressContract {
    interface View {

        fun getAct(): AddressActivity

        fun loadAddressCallback(addressItemCount: Int)

        fun createAddAddressAct(addressItem: AddressItem)
    }

    interface Model {

        fun loadAddress(isSelectMode: Boolean)

    }
}