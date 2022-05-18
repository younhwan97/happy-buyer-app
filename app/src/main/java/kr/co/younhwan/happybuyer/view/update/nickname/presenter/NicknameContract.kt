package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import kr.co.younhwan.happybuyer.view.update.UpdateActivity

interface NicknameContract{
    interface View {

        fun updateResultCallback(isSuccess:Boolean)

        fun getAct(): UpdateActivity

    }

    interface Model{

        fun updateUserNickname(newNickname: String)

    }
}