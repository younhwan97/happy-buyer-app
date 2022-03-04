package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class CategoryActivity : AppCompatActivity() {
    /* View Binding */
    lateinit var viewDataBinding: ActivityCategoryBinding

    /* Fragment list */
    val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // action -> tool bar
        setSupportActionBar(viewDataBinding.categoryToolbar)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

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

        viewDataBinding.categoryViewPager2.adapter = object : FragmentStateAdapter(this){
            override fun createFragment(position: Int)= fragmentList[position]
            override fun getItemCount() = fragmentList.size
        }

        // Tab과 view pager2를 연결
        var selectTab: TabLayout.Tab? = null
        TabLayoutMediator(viewDataBinding.tabs, viewDataBinding.categoryViewPager2) { tab: TabLayout.Tab, i: Int ->
            tab.text = label[i]
            if (i == position)
                selectTab = tab
        }.attach()

        viewDataBinding.run {
            tabs.selectTab(selectTab) // toolbar에 선택된 탭을 표기하도록 설정

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    categoryTitle.text = tab?.text
                }
            })
            categoryTitle.text = selectTab?.text
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_menu, menu)
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
            R.id.baksetIconInCategoryMenu-> {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}