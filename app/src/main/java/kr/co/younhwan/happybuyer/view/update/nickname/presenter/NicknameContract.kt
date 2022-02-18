package kr.co.younhwan.happybuyer.view.update.nickname.presenter

interface NicknameContract{
    interface View {
        fun updateSuccessCallback(nicknameToUpdate:String)

        fun updateFailCallback()
    }

    interface Model{
        fun updateUserNickname(nicknameToUpdate: String)
    }
}