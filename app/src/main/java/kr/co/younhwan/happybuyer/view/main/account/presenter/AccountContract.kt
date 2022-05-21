package kr.co.younhwan.happybuyer.view.main.account.presenter

import kr.co.younhwan.happybuyer.view.main.MainActivity

interface AccountContract {
    interface View {

        fun getAct(): MainActivity

        fun logoutWithKakaoCallback(isSuccess: Boolean, error: Throwable?)

        fun withdrawalWithKakaoCallback(isSuccess: Boolean, error: Throwable?)
    }

    interface Model {

        fun logoutWithKakao()

        fun withdrawalWithKakao()

    }
}