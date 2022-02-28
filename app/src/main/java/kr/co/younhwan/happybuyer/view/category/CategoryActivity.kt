package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding
import kr.co.younhwan.happybuyer.util.setupBadge
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class CategoryActivity : AppCompatActivity() {

    /* View Binding */
    lateinit var viewDataBinding: ActivityCategoryBinding

    // category view pager2에 세팅하기 위한 Fragment 들을 가지고 있는 ArrayList
    val fragmentList = ArrayList<Fragment>()

    /**/
    lateinit var sortBy : String

    var textCartItemCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 액션바 -> 툴바
        viewDataBinding.categoryToolbar.title = "코코마트"
        viewDataBinding.categoryToolbar.setTitleTextAppearance(this,
            R.style.ToolbarTitleTheme
        )
        setSupportActionBar(viewDataBinding.categoryToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Main Activity 로 부터 전달 받은 데이터
        val label = intent.getStringArrayListExtra("label")
        val position = intent.getIntExtra("position", 0)

        for (i in 1..label!!.size) {
            val category = CategoryFragment()
            val bundle = Bundle()
            bundle.putString("category", label[i-1])
            category.arguments = bundle
            fragmentList.add(category)
        }

        val adapter1 = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getItemCount(): Int {
                return fragmentList.size
            }
        }

        viewDataBinding.categoryViewPager2.adapter = adapter1


        // tab과 view pager2를 연결
        var selecetdTab: TabLayout.Tab? = null
        TabLayoutMediator(viewDataBinding.tabs, viewDataBinding.categoryViewPager2) { tab: TabLayout.Tab, i: Int ->
            tab.text = label[i]
            if (i == position)
                selecetdTab = tab
        }.attach()

        // toolbar에 선택된 탭을 표기하도록 설정
        viewDataBinding.tabs.selectTab(selecetdTab)
        viewDataBinding.categoryToolbar.title = label[position]
        val listener1 = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewDataBinding.categoryToolbar.title = label[viewDataBinding.tabs.selectedTabPosition]
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        }
        viewDataBinding.tabs.addOnTabSelectedListener(listener1)

        viewDataBinding.chip4.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.colorTheme
        ))
    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_menu, menu) // 메뉴 객체 생성 및 부착(적용)

        val menuItem = menu?.findItem(R.id.basketIconInMainMenu)
        val actionView = menuItem?.actionView
        textCartItemCount = actionView?.findViewById<TextView>(R.id.cart_badge)

        setupBadge(textCartItemCount)

        actionView?.setOnClickListener{
            onOptionsItemSelected(menuItem)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.searchIconInCategoryMenu -> {
                val categoryIntent = Intent(this, SearchActivity::class.java)
                startActivity(categoryIntent)
            }
            R.id.basketIconInMainMenu -> {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    // 툴바 설정 ----
    // -----------------------------------------------------
}