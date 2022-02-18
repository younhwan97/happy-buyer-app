package kr.co.younhwan.happybuyer.view.login.presenter

import android.content.Context

interface LoginContract{
    interface View {

        fun loginResultCallback(success: Boolean)

    }

    interface Model {

        fun loginWithKakao(context: Context)

    }
}