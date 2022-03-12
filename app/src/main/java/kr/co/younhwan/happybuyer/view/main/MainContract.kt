package kr.co.younhwan.happybuyer.view.main

interface MainContract {
    interface View {

        fun getAct() : MainActivity

    }

    interface Model {

        fun requestPermission()

    }
}