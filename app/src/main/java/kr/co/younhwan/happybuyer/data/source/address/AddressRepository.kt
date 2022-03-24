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

    // UPDATE
    override fun update(
        kakaoAccountId: Long,
        addressItem: AddressItem,
        updateCallback: AddressSource.UpdateCallback?
    ) {
        addressRemoteDataSource.update(
            kakaoAccountId,
            addressItem,
            object : AddressSource.UpdateCallback {
                override fun onUpdate(isSuccess: Boolean) {
                    updateCallback?.onUpdate(isSuccess)
                }
            }
        )
    }

    // DELETE
    override fun delete(
        kakaoAccountId: Long,
        addressId: Int,
        deleteCallback: AddressSource.DeleteCallback?
    ) {
        addressRemoteDataSource.delete(
            kakaoAccountId,
            addressId,
            object : AddressSource.DeleteCallback {
                override fun onDelete(isSuccess: Boolean) {
                    deleteCallback?.onDelete(isSuccess)
                }
            }
        )
    }
}