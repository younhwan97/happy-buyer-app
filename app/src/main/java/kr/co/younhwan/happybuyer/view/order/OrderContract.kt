package kr.co.younhwan.happybuyer.view.order

import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem

interface OrderContract {
    interface View {

        fun getAct(): OrderActivity

        fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?)
    }

    interface Model {

        fun loadDefaultAddress()

        fun setOrderProduct(list: ArrayList<BasketItem>)
    }
}