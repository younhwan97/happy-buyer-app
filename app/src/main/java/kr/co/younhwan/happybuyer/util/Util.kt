package kr.co.younhwan.happybuyer.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

// Fragment replace 를 처리하는 확장함수
fun AppCompatActivity.replace(@IdRes frameId: Int, fragment:androidx.fragment.app.Fragment, tag:String? = null){
    supportFragmentManager.beginTransaction().replace(frameId, fragment, tag).commit()
}