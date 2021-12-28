package kr.co.younhwan.happybuyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.Navigation.*
import kr.co.younhwan.happybuyer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    // View Binding
    lateinit var mainActivityBinding : ActivityMainBinding

    // 권한 목록
    private val permissionList = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    // MainActivity 에서 사용할 프래그먼트
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val accountFragment = AccountFragment()

    // 툴바의 search item
    // 추후 추후 프래그먼트 전환 시 강제로 expand 하기 위해서 필요
    var searchItem: MenuItem? = null

    // kakao token
    var kakaoAccountId : Long? = null

    // 어플리케이션이 실행되고 단 1번 호출!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 객체 생성
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)

        // Splash Delay
        SystemClock.sleep(1000)

        // Splash 화면 이후로 보여질 화면을 설정
        setTheme(R.style.Theme_HappyBuyer)
        setContentView(mainActivityBinding.root)

        // 액션바 -> 툴바
        mainActivityBinding.mainToolbar.title = "HappyBuyer/코코마트"
        mainActivityBinding.mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(mainActivityBinding.mainToolbar)

        // 권한 요청
        requestPermissions(permissionList, 0)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 = 로그인 정보가 없을 때

            } else if (tokenInfo != null) {
                kakaoAccountId= tokenInfo.id
            }
        }

        // 바텀 내비게이션의 이벤트 리스너를 설정
        mainActivityBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    setFragment("home")
                    true
                }
                R.id.action_search -> {
                    searchItem?.expandActionView()
                    true
                }
                R.id.action_account -> {
                    if(kakaoAccountId != null)
                        setFragment("account")
                    else
                        setFragment("login")
                    true
                }
                else -> false
            }
        }

        // 초기 화면의 프래그먼트를 설정
        setFragment("home")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            0 -> {
                if(resultCode == RESULT_CANCELED){
                    mainActivityBinding.bottomNavigation.selectedItemId = R.id.action_home
                }
            }
        }
    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    // 툴바의 메뉴 아이템을 생성하고, 이벤트 리스너를 장착한다.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 메뉴 객체 생성
        menuInflater.inflate(R.menu.main_menu, menu)

        // 메뉴의 search view 에 이벤트 리스너 설정을 위해 객체를 얻어온다.
        searchItem = menu?.findItem(R.id.search_item)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        // setting search view
        searchView.queryHint = "검색어를 입력하세요."

        // searchView 가 펼쳐졌을 때 발생하는 이벤트를 처리하는 리스너
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                mainActivityBinding.bottomNavigation.visibility = View.VISIBLE
                mainActivityBinding.bottomNavigation.selectedItemId = R.id.action_home
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                mainActivityBinding.bottomNavigation.visibility = View.GONE
                setFragment("search")
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

        searchItem?.setOnActionExpandListener(expandListener)
        searchView.setOnQueryTextListener(inputListener)
        return true
    }

    // 툴바가 클릭됐을 때 발생하는 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_item -> {
                // CollapseActionView 설정에 따라 뷰가 확장되고 TextEdit view가 나타날 것 이다.
            }
            R.id.shopping_basket_item -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
    // 툴바 설정 ----
    // -----------------------------------------------------

    // Fragment Controller
    fun setFragment(requestFragment: String) {
        val tran = supportFragmentManager.beginTransaction()

        when (requestFragment) {
            "home" -> {
                title = "HappyBuyer/코코마트"
                tran.replace(R.id.mainContainer, homeFragment)
            }
            "search" -> {
                title = "검색"
                tran.replace(R.id.mainContainer, searchFragment)
            }
            "account" -> {
                title = "내 정보"
                tran.replace(R.id.mainContainer, accountFragment)
            }
            "login" -> {
                val login_intent = Intent(this, LoginActivity::class.java)
                startActivityForResult(login_intent, 0)
            }
        }

        tran.commit()
    }
}