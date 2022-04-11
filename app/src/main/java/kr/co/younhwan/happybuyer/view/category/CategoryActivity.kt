package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class CategoryActivity : AppCompatActivity() {
    lateinit var viewDataBinding: ActivityCategoryBinding

    val fragmentList = ArrayList<Fragment>() // Viewpager2에 셋팅하기 위한 프래그먼트를 가지고 있는 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 인텐트에서 데이터 추출
        val initPosition = intent.getIntExtra("init_position", -1)
        val label = if (intent.hasExtra("label")) {
            intent.getStringArrayListExtra("label")
        } else {
            null
        }

        if (label == null || initPosition == -1) {
            finish()
        } else {
            // 툴바
            viewDataBinding.categoryToolbar.setNavigationOnClickListener {
                finish()
            }

            viewDataBinding.categoryToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.searchIconInCategoryMenu -> {
                        val categoryIntent = Intent(this, SearchActivity::class.java)
                        startActivity(categoryIntent)
                        true
                    }
                    R.id.baksetIconInCategoryMenu -> {
                        val basketIntent = Intent(this, BasketActivity::class.java)
                        startActivity(basketIntent)
                        true
                    }
                    else -> false
                }
            }

            // ViewPager2에 셋팅할 프래그먼트 생성
            for (i in 1..label.size) {
                val frag = CategoryFragment()
                val bundle = Bundle()
                bundle.putString("category", label[i - 1])
                frag.arguments = bundle
                fragmentList.add(frag)
            }

            // ViewPager2의 어댑터 셋팅
            viewDataBinding.categoryViewPager2.adapter = object : FragmentStateAdapter(this) {
                override fun createFragment(position: Int) = fragmentList[position]
                override fun getItemCount() = fragmentList.size
            }

            viewDataBinding.categoryViewPager2.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            viewDataBinding.categoryViewPager2.reduceDragSensitivity()

            // ViewPager2와 Tab 버튼을 연결
            var selectTab: TabLayout.Tab? = null
            TabLayoutMediator(
                viewDataBinding.tabs,
                viewDataBinding.categoryViewPager2
            ) { tab: TabLayout.Tab, position: Int ->

                tab.text = label[position] // Tab 이름

                if (position == initPosition) {
                    selectTab = tab
                    viewDataBinding.categoryToolbar.title = tab.text
                }
            }.attach()

            // Init Tab 및 이벤트 리스너 셋팅
            viewDataBinding.tabs.selectTab(selectTab)
            viewDataBinding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewDataBinding.categoryToolbar.title = tab?.text
                    selectTab = tab
                }
            })
        }
    }

    fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*8)       // "8" was obtained experimentally
    }
}