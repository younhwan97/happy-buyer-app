package kr.co.younhwan.happybuyer.data.source.user

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken

interface UserSource {

    interface createUserCallback {
        fun onCreateUser(isSuccess: Boolean)
    }

    fun createUser(kakaoLoginId: Long?, kakaoNickname:String?, createUserCallback: createUserCallback?)
}