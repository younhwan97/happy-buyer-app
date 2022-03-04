package kr.co.younhwan.happybuyer.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var searchActivityBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 객체 생성
        searchActivityBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchActivityBinding.root)

        // 액션바 -> 툴바
        searchActivityBinding.searchToolbar.title = ""
        setSupportActionBar(searchActivityBinding.searchToolbar)

    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    // 툴바의 메뉴 아이템을 생성하고, 이벤트 리스너를 장착한다.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴 객체 생성
        menuInflater.inflate(R.menu.search_menu, menu)

        // 메뉴의 search view 에 이벤트 리스너 설정을 위해 객체를 얻어온다.
        val searchItem = menu?.findItem(R.id.searchIconInSearchMenu)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        // setting search view
        searchView.queryHint = "검색어를 입력하세요."
        searchItem.expandActionView()

        // searchView 가 펼쳐졌을 때 발생하는 이벤트를 처리하는 리스너
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                finish()
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }
        }

        // searchView 에 text 가 입력 됐을 때 발생하는 이벤트를 처리하는 리스너
        val inputListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        }

        searchItem.setOnActionExpandListener(expandListener)
        searchView.setOnQueryTextListener(inputListener)
        return true
    }
    // 툴바 설정 ----
    // -----------------------------------------------------
}