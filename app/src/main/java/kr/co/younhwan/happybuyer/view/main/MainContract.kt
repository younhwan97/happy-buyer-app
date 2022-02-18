package kr.co.younhwan.happybuyer.view.main

interface MainContract {
    interface View {

    }

    interface Model {
        fun loadMainScreen(activity: MainActivity)

        fun requestPermission(activity: MainActivity)

        fun loadUser(activity: MainActivity)
    }
}