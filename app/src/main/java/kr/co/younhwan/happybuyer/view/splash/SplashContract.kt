package kr.co.younhwan.happybuyer.view.splash

interface SplashContract{

    interface View{

        fun getAct() : SplashActivity

        fun finishSplashAct()

    }

    interface Model{

        fun loadUserInfo()

    }
}