package kr.co.younhwan.happybuyer.data.source.address

import kr.co.younhwan.happybuyer.data.AddressItem

interface AddressSource {
    // CREATE
    fun create(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        createCallback: CreateCallback?
    )

    interface CreateCallback {
        fun onCreate(addressId: Int)
    }

    // READ
    fun read(
        kakaoAccountId: Long,
        readCallback: ReadCallback?
    )

    interface ReadCallback {
        fun onRead(list: ArrayList<AddressItem>)
    }
}