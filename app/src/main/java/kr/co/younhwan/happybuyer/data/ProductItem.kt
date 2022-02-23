package kr.co.younhwan.happybuyer.data

data class ProductItem(val productId: Int, val productImageUrl: String, val productName: String, val productPrice: Int, var isWished: Boolean = false, var count: Int = -1)