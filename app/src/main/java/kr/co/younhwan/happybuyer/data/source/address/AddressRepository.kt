package kr.co.younhwan.happybuyer.data.source.address

import kr.co.younhwan.happybuyer.data.AddressItem

object AddressRepository : AddressSource {

    private val addressRemoteDataSource = AddressRemoteDataSource

    // CREATE
    override fun create(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        createCallback: AddressSource.CreateCallback?
    ) {
        addressRemoteDataSource.create(
            kakaoAccountId,
            addressItem,
            object : AddressSource.CreateCallback {
                override fun onCreate(addressId: Int) {
                    createCallback?.onCreate(addressId)
                }
            })
    }

    // READ
    override fun read(
        kakaoAccountId: Long,
        readCallback: AddressSource.ReadCallback?
    ) {
        addressRemoteDataSource.read(
            kakaoAccountId,
            object : AddressSource.ReadCallback {
                override fun onRead(list: ArrayList<AddressItem>) {
                    readCallback?.onRead(list)
                }
            }
        )
    }
}