package kr.co.younhwan.happybuyer.view.main

import kr.co.younhwan.happybuyer.GlobalApplication

interface MainContract {
    interface View {

    }

    interface Model {
        fun loadMainScreen(act: MainActivity)

        fun requestPermission(act: MainActivity)

        fun loadUser(app: GlobalApplication)
    }
}