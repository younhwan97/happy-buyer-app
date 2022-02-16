package kr.co.younhwan.happybuyer.util

import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication

// Fragment replace 를 처리하는 확장함수
fun AppCompatActivity.replace(@IdRes frameId: Int, fragment:androidx.fragment.app.Fragment, tag:String? = null){
    supportFragmentManager.beginTransaction().replace(frameId, fragment, tag).commit()
}