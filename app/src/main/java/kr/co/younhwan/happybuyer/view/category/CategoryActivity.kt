package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding
import kr.co.younhwan.happybuyer.util.reduceDragSensitivity
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class CategoryActivity : AppCompatActivity() {
    lateinit var viewDataBinding: ActivityCategoryBinding

    val fragmentList = ArrayList<Fragment>()

    var sortBy : String = "추천순"

    private lateinit var notificationBadgeOfBasketMenu: TextView

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
            // 툴바 & 메뉴
            viewDataBinding.categoryToolbar.setNavigationOnClickListener {
                finish()
            }

            viewDataBinding.categoryToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.searchInCategory -> {
                        val categoryIntent = Intent(this, SearchActivity::class.java)
                        startActivity(categoryIntent)
                        true
                    }
                    else -> false
                }
            }

            val menu = viewDataBinding.categoryToolbar.menu
            val basketItem = menu.findItem(R.id.basketInCategory)
            val actionView = basketItem.actionView

            actionView.setOnClickListener {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }

            notificationBadgeOfBasketMenu = actionView.findViewById(R.id.cart_badge)
            setNotificationBadge()

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

            // ViewPager2 스크롤 민감도 조절 및 오버스크롤 설정
            viewDataBinding.categoryViewPager2.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            viewDataBinding.categoryViewPager2.reduceDragSensitivity()

            // ViewPager2와 Tab 버튼을 연결
            var initTab: TabLayout.Tab? = null
            TabLayoutMediator(
                viewDataBinding.categoryTabs,
                viewDataBinding.categoryViewPager2
            ) { tab: TabLayout.Tab, position: Int ->

                tab.text = label[position] // Tab 이름

                if (position == initPosition) {
                    initTab = tab
                    viewDataBinding.categoryToolbar.title = tab.text
                }
            }.attach()

            // Init Tab 및 이벤트 리스너 셋팅
            viewDataBinding.categoryTabs.selectTab(initTab)
            viewDataBinding.categoryTabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewDataBinding.categoryToolbar.title = tab?.text
                }
            })
        }
    }

    fun setNotificationBadge() {
        notificationBadgeOfBasketMenu.visibility =
            if ((application as GlobalApplication).basketItemCount > 0) View.VISIBLE else View.GONE
    }
}