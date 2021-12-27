package kr.co.younhwan.happybuyer

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding

class CategoryActivity : FragmentActivity() {
    // View Binding
    lateinit var categoryActivityBinding: ActivityCategoryBinding

    // category view pager2에 세팅하기 위한 Fragment들을 가지고 있는 ArrayList
    val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryActivityBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryActivityBinding.root)

        // 액션바 -> 툴바
        setActionBar(categoryActivityBinding.categoryToolbar)

        // 툴바 세팅
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        categoryActivityBinding.categoryToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)

        // MainActivity로 부터 전달 받은 데이터
        val label = intent.getStringArrayExtra("label")
        val position = intent.getIntExtra("position", 0)

        for (i in 1..label!!.size) {
            val category = CategoryFragment()
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

        categoryActivityBinding.categoryViewPager2.adapter = adapter1


        // tab과 view pager2를 연결
        var selecetdTab: TabLayout.Tab? = null
        TabLayoutMediator(categoryActivityBinding.tabs, categoryActivityBinding.categoryViewPager2) { tab: TabLayout.Tab, i: Int ->
            tab.text = label[i]
            if (i == position)
                selecetdTab = tab
        }.attach()

        // toolbar에 선택된 탭을 표기하도록 설정
        categoryActivityBinding.tabs.selectTab(selecetdTab)
        actionBar?.title = label[position]
        val listener1 = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                actionBar?.title = label[categoryActivityBinding.tabs.getSelectedTabPosition()]
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        }
        categoryActivityBinding.tabs.addOnTabSelectedListener(listener1)

        categoryActivityBinding.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//        categoryActivityBinding.selectListOrderChipGroup.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.chip4->{
//                    group.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//                    group.chip5.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip6.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip7.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip8.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                }
//                R.id.chip5->{
//                    group.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip5.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//                    group.chip6.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip7.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip8.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                }
//                R.id.chip6->{
//                    group.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip5.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip6.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//                    group.chip7.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip8.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                }
//                R.id.chip7->{
//                    group.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip5.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip6.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip7.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//                    group.chip8.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                }
//                R.id.chip8->{
//                    group.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip5.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip6.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip7.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDivision))
//                    group.chip8.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
//                }
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
