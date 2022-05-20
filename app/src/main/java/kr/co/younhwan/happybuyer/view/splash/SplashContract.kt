package kr.co.younhwan.happybuyer.view.splash

interface SplashContract {

    interface View {

        fun getAct(): SplashActivity

        fun loadUserInfoCallback()

    }

    interface Model {

        fun loadUserInfo()

    }
}