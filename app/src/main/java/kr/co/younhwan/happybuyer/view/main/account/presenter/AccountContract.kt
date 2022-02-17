package kr.co.younhwan.happybuyer.view.main.account.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.view.main.MainActivity
import java.lang.Error

interface AccountContract {
    interface View {
        fun logoutSuccessCallback()

        fun logoutFailCallback(error: Throwable?)

        fun withdrawalSuccessCallback()

        fun withdrawalFailCallback(error: Throwable?)
    }

    interface Model {
        fun logoutWithKakao(context: Context, activity: MainActivity)

        fun withdrawalWithKakao(context: Context, activity: MainActivity)
    }
}