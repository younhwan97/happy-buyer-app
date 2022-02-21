package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.GlobalApplication

interface NicknameContract{
    interface View {

        fun updateResultCallback(success:Boolean)

        fun getApp(): GlobalApplication

    }

    interface Model{
        fun updateUserNickname(newNickname: String)
    }
}