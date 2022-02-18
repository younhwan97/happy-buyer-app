package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.GlobalApplication

interface NicknameContract{
    interface View {

        fun updateResultCallback(success:Boolean)

    }

    interface Model{
        fun updateUserNickname(kakaoAccountId:Long, newNickname: String, app:GlobalApplication)
    }
}