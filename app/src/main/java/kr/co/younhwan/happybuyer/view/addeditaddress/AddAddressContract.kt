package kr.co.younhwan.happybuyer.view.addeditaddress

import kr.co.younhwan.happybuyer.data.AddressItem

interface AddAddressContract {
    interface View {

        fun checkHasDefaultAddressCallback(hasDefaultAddress: Boolean)

        fun getAct(): AddAddressActivity

        fun checkValidation()


        fun addAddressCallback(addressId: Int, addressItem: AddressItem)
    }

    interface Model {

        fun checkHasDefaultAddress()

        fun addAddress(addressItem: AddressItem)

    }
}