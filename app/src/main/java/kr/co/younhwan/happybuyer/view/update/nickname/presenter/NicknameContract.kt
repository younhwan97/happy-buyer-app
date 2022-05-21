package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import kr.co.younhwan.happybuyer.view.update.UpdateActivity

interface NicknameContract {
    interface View {

        fun getAct(): UpdateActivity

        fun updateUserNicknameCallback(isSuccess: Boolean)

    }

    interface Model {

        fun updateUserNickname(newNickname: String)

    }
}