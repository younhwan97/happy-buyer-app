package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class NicknamePresenter(
    val view: NicknameContract.View,
    private val userData: UserRepository
) : NicknameContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun updateUserNickname(newNickname: String) {
        if (app.isLogined) {
            userData.update(
                kakaoAccountId = app.kakaoAccountId,
                updateTarget = "nickname",
                newContent = newNickname,
                updateCallback = object : UserSource.UpdateCallback {
                    override fun onUpdate(isSuccess: Boolean) {
                        if (isSuccess)
                            app.nickname = newNickname

                        view.updateUserNicknameCallback(isSuccess)
                    }
                })
        } else {
            view.updateUserNicknameCallback(false)
        }
    }
}