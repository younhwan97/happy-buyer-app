package kr.co.younhwan.happybuyer.view.update.point.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class PointPresenter(
    val view: PointContract.View,
    private val userData: UserRepository
) : PointContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun updateUserPoint(newPoint: String) {
        if (app.isLogined) {
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
            view.updateUserPointCallback(false)
        }
    }
}