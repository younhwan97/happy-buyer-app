package kr.co.younhwan.happybuyer

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    // 유저 정보
    var isLogined = false
    var kakaoAccountId: Long? = null
    var nickname: String? = null
    var pointNumber: Int? = null
    var shippingAddress: String? = null
    var activatedBasket: String? = "deactivate"

    // 장바구니
    var basketItemCount : Int = 0

    // 찜
    var wishedProductId : ArrayList<Int> = ArrayList<Int>()

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "50b9d4b20edf926f1d3466c8aed65d17")
    }
}