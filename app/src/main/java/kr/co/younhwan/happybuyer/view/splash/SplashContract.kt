package kr.co.younhwan.happybuyer.view.splash

interface SplashContract{

    interface View{

        fun finishSplashActivity()

        fun getAct() : SplashActivity
    }

    interface Model{

        fun loadUserInfo()

    }
}