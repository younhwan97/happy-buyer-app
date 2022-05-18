package kr.co.younhwan.happybuyer.view.update.nickname.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class NicknamePresenter(
    val view: NicknameContract.View,
    private val userData: UserRepository
) : NicknameContract.Model {

    override fun updateUserNickname(newNickname: String) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            // 로그인 상태일 때
            userData.update(
                kakaoAccountId = app.kakaoAccountId,
                updateTarget = "nickname",
                newContent = newNickname,
                updateCallback = object : UserSource.UpdateCallback {
                    override fun onUpdate(isSuccess: Boolean) {
                        if (isSuccess)
                            app.nickname = newNickname

                        view.updateResultCallback(isSuccess)
                    }
                })
        } else {
            // 로그인 상태가 아닐 때
            view.updateResultCallback(false)
        }
    }
}