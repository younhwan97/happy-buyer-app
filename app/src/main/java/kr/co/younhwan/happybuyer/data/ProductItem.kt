package kr.co.younhwan.happybuyer.data

import android.os.Parcel
import android.os.Parcelable

data class ProductItem (
    val productId: Int,            // 상품 id
    val productImageUrl: String,   // 상품 이미지 url
    val productName: String,       // 상품 이름
    val productPrice: Int,         // 상품 가격 
    var isWished: Boolean = false, // 찜한 상품인지 여부
    var countInBasket: Int = 0,    // 장바구니에 담긴 개수
    var onSale: Boolean = false,   // 할인 이벤트 중인 상품인지
    var eventPrice: Int = 0,   // 할인된 가격
    var sales: Int = 0             // 누적 판매량
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeString(productImageUrl)
        parcel.writeString(productName)
        parcel.writeInt(productPrice)
        parcel.writeByte(if (isWished) 1 else 0)
        parcel.writeInt(countInBasket)
        parcel.writeByte(if (onSale) 1 else 0)
        parcel.writeInt(eventPrice)
        parcel.writeInt(sales)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItem> {
        override fun createFromParcel(parcel: Parcel): ProductItem {
            return ProductItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductItem?> {
            return arrayOfNulls(size)
        }
    }
}