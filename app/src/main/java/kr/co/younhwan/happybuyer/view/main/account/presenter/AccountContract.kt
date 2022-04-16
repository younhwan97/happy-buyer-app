package kr.co.younhwan.happybuyer.view.main.account.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.main.MainActivity
import java.lang.Error

interface AccountContract {
    interface View {

        fun getAct() : MainActivity

        fun logoutResultCallback(success: Boolean, error: Throwable?)

        fun withdrawalResultCallback(success: Boolean, error: Throwable?)
    }

    interface Model {

        fun logoutWithKakao()

        fun withdrawalWithKakao()

    }
}