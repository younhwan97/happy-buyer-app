package kr.co.younhwan.happybuyer.data

data class ProductItem(
    val productId: Int,            // 상품 id
    val productImageUrl: String,   // 상품 이미지 url
    val productName: String,       // 상품 이름
    val productPrice: Int,         // 상품 가격 
    var isWished: Boolean = false, // 찜한 상품인지 여부
    var countInBasket: Int = 0,    // 장바구니에 담긴 개수
    var onSale: Boolean = false,   // 할인 이벤트 중인 상품인지
    var eventPrice: Int = 0,   // 할인된 가격
    var sales: Int = 0             // 누적 판매량
)