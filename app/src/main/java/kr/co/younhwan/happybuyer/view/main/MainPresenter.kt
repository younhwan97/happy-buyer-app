package kr.co.younhwan.happybuyer.view.main

import android.Manifest

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Model {

    override fun requestPermission() {
        view.getAct().requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            ), 0
        )
    }
}