package kr.co.younhwan.happybuyer

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    // 유저 정보
    var isLogined = false
    var kakaoAccountId: Long? = -1L // oAuth value
    var nickname: String? = "-"
    var pointNumber: Int? = 0
    var shippingAddress: String? = "-"

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "50b9d4b20edf926f1d3466c8aed65d17")
    }
}