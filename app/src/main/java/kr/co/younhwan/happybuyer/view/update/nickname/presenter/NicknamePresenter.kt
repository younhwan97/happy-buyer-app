package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class NicknamePresenter(
    val view: NicknameContract.View,
    private val userData: UserRepository,
) : NicknameContract.Model {

    override fun updateUserNickname(newNickname: String) {
        val app = view.getApp()
        val kakaoAccountId = app.kakaoAccountId!!
        userData.updateUser(
            kakaoAccountId,
            newNickname,
            object : UserSource.updateUserCallback {
                override fun onUpdateUser(isSuccess: Boolean) {
                    if (isSuccess) {
                        app.nickname = newNickname
                        view.updateResultCallback(true)
                    } else {
                        view.updateResultCallback(false)
                    }
                }
            })
    }
}