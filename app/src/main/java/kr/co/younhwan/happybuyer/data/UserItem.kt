package kr.co.younhwan.happybuyer.data

data class UserItem(
    val kakaoAccountId: Long,
    val nickname: String,
    val pointNumber: Int,
    val shippingAddress: String?,
    val activatedBasket: String
)