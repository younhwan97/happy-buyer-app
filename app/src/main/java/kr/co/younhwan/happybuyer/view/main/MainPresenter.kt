package kr.co.younhwan.happybuyer.view.main

import android.Manifest
import android.os.SystemClock
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.UserItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource
import kr.co.younhwan.happybuyer.util.setupBadge

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