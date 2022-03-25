package kr.co.younhwan.happybuyer.view.order

import kr.co.younhwan.happybuyer.data.AddressItem

interface OrderContract {
    interface View {

        fun getAct(): OrderActivity

        fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?)
    }

    interface Model {

        fun loadDefaultAddress()

    }
}