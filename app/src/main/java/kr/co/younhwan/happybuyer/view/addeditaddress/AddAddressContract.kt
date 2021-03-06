package kr.co.younhwan.happybuyer.view.addeditaddress

import kr.co.younhwan.happybuyer.data.AddressItem

interface AddAddressContract {
    interface View {

        fun getAct(): AddAddressActivity

        fun checkHasDefaultAddressCallback(hasDefaultAddress: Boolean)

        fun checkValidation()

        fun addAddressCallback(addressItem: AddressItem, isSuccess: Boolean)

        fun deleteAddressCallback(isSuccess: Boolean)

    }

    interface Model {

        fun checkHasDefaultAddress()

        fun addAddress(addressItem: AddressItem)

        fun deleteAddress(addressId: Int)

    }
}