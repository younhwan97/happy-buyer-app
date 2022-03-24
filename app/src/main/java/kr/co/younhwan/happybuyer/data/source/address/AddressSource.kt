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

    // UPDATE
    fun update(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        updateCallback: UpdateCallback?
    )

    interface UpdateCallback {
        fun onUpdate(isSuccess: Boolean)
    }

    // DELETE
    fun delete(
        kakaoAccountId: Long,
        addressId: Int,
        deleteCallback: DeleteCallback?
    )

    interface DeleteCallback {
        fun onDelete(isSuccess: Boolean)
    }
}