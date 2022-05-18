package kr.co.younhwan.happybuyer.view.update.point.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class PointPresenter(
    val view: PointContract.View,
    private val userData: UserRepository
) : PointContract.Model {

    override fun updateUserPoint(newPoint: String) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            // 로그인 상태일 때
            userData.update(
                kakaoAccountId = app.kakaoAccountId,
                updateTarget = "point",
                newContent = newPoint,
                updateCallback = object : UserSource.UpdateCallback {
                    override fun onUpdate(isSuccess: Boolean) {
                        if (isSuccess)
                            app.point = newPoint

                        view.updateUserPointCallback(isSuccess)
                    }
                }
            )
        } else {
            // 로그인 상태가 아닐 때
            view.updateUserPointCallback(false)
        }
    }
}