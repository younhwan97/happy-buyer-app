package kr.co.younhwan.happybuyer.view.main

import android.content.Context

interface MainContract {
    interface View {

        fun getAct() : MainActivity

    }

    interface Model {
        fun loadMainScreen()

        fun requestPermission()

        fun loadUserInfo()
    }
}