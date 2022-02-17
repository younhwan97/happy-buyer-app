package kr.co.younhwan.happybuyer.view.login.presenter

import android.content.Context

interface LoginContract{
    interface View {
        fun loginSuccessCallback()

        fun loginFailCallback()
    }

    interface Model {
        fun loginWithKakao(context: Context)
    }
}