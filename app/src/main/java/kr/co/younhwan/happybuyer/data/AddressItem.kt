package kr.co.younhwan.happybuyer.data

import android.os.Parcel
import android.os.Parcelable

data class AddressItem(
    var addressId: Int,
    val address: String?,
    val addressPhone: String?,
    val addressReceiver: String?,
    var isDefault: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(addressId)
        parcel.writeString(address)
        parcel.writeString(addressPhone)
        parcel.writeString(addressReceiver)
        parcel.writeValue(isDefault)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressItem> {
        override fun createFromParcel(parcel: Parcel): AddressItem {
            return AddressItem(parcel)
        }

        override fun newArray(size: Int): Array<AddressItem?> {
            return arrayOfNulls(size)
        }
    }

}