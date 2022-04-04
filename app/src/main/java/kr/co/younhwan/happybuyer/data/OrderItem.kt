package kr.co.younhwan.happybuyer.data

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(
    var orderId: Int,
    val name: String,
    val status: String,
    val date: String?,
    val receiver: String,
    val phone: String,
    val address: String,
    val requirement: String?,
    val point: String?,
    val detectiveHandlingMethod: String,
    val payment: String,
    val originalPrice: String,
    val eventPrice: String,
    val bePaidPrice: String,
    val products: ArrayList<BasketItem>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        ArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(orderId)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(date)
        parcel.writeString(receiver)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(requirement)
        parcel.writeString(point)
        parcel.writeString(detectiveHandlingMethod)
        parcel.writeString(payment)
        parcel.writeString(originalPrice)
        parcel.writeString(eventPrice)
        parcel.writeString(bePaidPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }

}
