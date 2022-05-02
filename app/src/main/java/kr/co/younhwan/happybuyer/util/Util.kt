package kr.co.younhwan.happybuyer.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

// Fragment replace 를 처리하는 확장함수
fun AppCompatActivity.replace(
    @IdRes frameId: Int,
    fragment: androidx.fragment.app.Fragment,
    tag: String? = null
) {
    supportFragmentManager.beginTransaction().replace(frameId, fragment, tag).commit()
}

// Viewpager2 의 스크롤 민감도를 조절하는 확장함수
fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 8)       // "8" was obtained experimentally
}