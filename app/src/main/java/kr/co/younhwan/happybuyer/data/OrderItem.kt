package kr.co.younhwan.happybuyer.data

data class OrderItem(
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
)
