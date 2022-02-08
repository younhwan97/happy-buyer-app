package kr.co.younhwan.happybuyer

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    // View Binding
    lateinit var categoryActivityBinding: ActivityCategoryBinding

    // category view pager2에 세팅하기 위한 Fragment 들을 가지고 있는 ArrayList
    val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryActivityBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryActivityBinding.root)

        // 액션바 -> 툴바
        categoryActivityBinding.categoryToolbar.title = "코코마트"
        categoryActivityBinding.categoryToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(categoryActivityBinding.categoryToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Main Activity로 부터 전달 받은 데이터
        val label = intent.getStringArrayExtra("label")
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
        categoryActivityBinding.categoryToolbar.title = label[position]
        val listener1 = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                categoryActivityBinding.categoryToolbar.title = label[categoryActivityBinding.tabs.selectedTabPosition]
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        }
        categoryActivityBinding.tabs.addOnTabSelectedListener(listener1)

        categoryActivityBinding.chip4.chipBackgroundColor =  ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorTheme))
    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // 메뉴 객체 생성 및 부착(적용)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.search_item_in_main -> {
                setFragment("search")
            }
            R.id.basket_item_in_main -> {
                setFragment("basket")
            }
        }

        return super.onOptionsItemSelected(item)
    }
    // 툴바 설정 ----
    // -----------------------------------------------------

    private fun setFragment(requestFragment: String){
        when(requestFragment){
            "search" -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }

            "basket" -> {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }

        }
    }
}





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