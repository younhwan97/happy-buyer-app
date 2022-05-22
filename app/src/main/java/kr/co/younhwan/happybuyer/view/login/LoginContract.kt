package kr.co.younhwan.happybuyer.view.login

import android.content.Context

interface LoginContract {
    interface View {

        fun loginWithKakaoCallback(isSuccess: Boolean)

    }

    interface Model {

        fun loginWithKakao(context: Context)

    }
}