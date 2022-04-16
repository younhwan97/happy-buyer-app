package kr.co.younhwan.happybuyer.view.update.point.presenter

import kr.co.younhwan.happybuyer.view.update.UpdateActivity

interface PointContract {
    interface View {

        fun getAct(): UpdateActivity

        fun updateUserPointCallback(isSuccess: Boolean)

    }

    interface Model {

        fun updateUserPoint(newPoint: String)

    }
}