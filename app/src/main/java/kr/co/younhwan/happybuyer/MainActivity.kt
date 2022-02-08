package kr.co.younhwan.happybuyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    // MainActivity 에서 사용할 프래그먼트 wetwerqewr
    private val homeFragment = HomeFragment()
    private val favoriteFragment = FavoriteFragment()
    private val accountFragment = AccountFragment()

    // 툴바의 search item
    // 추후 추후 프래그먼트 전환 시 강제로 expand 하기 위해서 필요
    var searchItem: MenuItem? = null

    // kakao token
    var kakaoAccountId : Long? = null
    var kakaoAccountNickname : String? = null

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
        mainActivityBinding.mainToolbar.title = "코코마트"
        mainActivityBinding.mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(mainActivityBinding.mainToolbar)

        // 권한 요청
        requestPermissions(permissionList, 0)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 = 로그인 정보가 없을 때

            } else if (tokenInfo != null) {
                kakaoAccountId= tokenInfo.id
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("kakao", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        kakaoAccountNickname = user.kakaoAccount?.profile?.nickname
                        Log.d("kakao","${kakaoAccountNickname}")
                    }
                }
            }
        }

        // 바텀 내비게이션의 이벤트 리스너를 설정
        mainActivityBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    setFragment("home")
                    true
                }
                R.id.action_favorite -> {
                    setFragment("favorite")
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

        // 첫 프래그먼트 설정
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // 메뉴 객체 생성 및 부착(적용)
        return true
    }

    // 툴바의 아이템이 클릭됐을 때 발생하는 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

    // Fragment Controller
    private fun setFragment(requestFragment: String) {
        val tran = supportFragmentManager.beginTransaction()

        when (requestFragment) {
            "home" -> {
                mainActivityBinding.mainToolbar.title = "코코마트"
                tran.replace(R.id.mainContainer, homeFragment)
            }
            "favorite" -> {
                mainActivityBinding.mainToolbar.title = "관심"
                tran.replace(R.id.mainContainer, favoriteFragment)
            }
            "account" -> {
                mainActivityBinding.mainToolbar.title = "내 정보"
                tran.replace(R.id.mainContainer, accountFragment)
            }
            "login" -> {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivityForResult(loginIntent, 0)
            }
            "search" -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }
            "basket" -> {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }
        }

        tran.commit()
    }
}