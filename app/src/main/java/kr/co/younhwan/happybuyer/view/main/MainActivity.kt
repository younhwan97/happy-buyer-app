package kr.co.younhwan.happybuyer.view.main

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.view.search.SearchActivity
import kr.co.younhwan.happybuyer.databinding.ActivityMainBinding
import kr.co.younhwan.happybuyer.util.replace
import kr.co.younhwan.happybuyer.view.main.account.AccountFragment
import kr.co.younhwan.happybuyer.view.main.favorite.FavoriteFragment
import kr.co.younhwan.happybuyer.view.main.home.HomeFragment

class MainActivity : AppCompatActivity() {
    /* View Binding */
    lateinit var viewDataBinding : ActivityMainBinding

    /* MainActivity 에서 사용할 프래그먼트 */
    private val homeFragment : HomeFragment by lazy {
        HomeFragment()
    }
    private val favoriteFragment : FavoriteFragment by lazy {
        FavoriteFragment()
    }
    private val accountFragment : AccountFragment by lazy {
        AccountFragment()
    }

    /* Method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 객체 생성
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)

        // 1초동안 스플래쉬 화면이 보여지도록 설정
        SystemClock.sleep(1000)

        // 스플래쉬 화면 이후로 보여질 화면을 설정
        setTheme(R.style.Theme_HappyBuyer)
        setContentView(viewDataBinding.root)

        // 액션바 -> 툴바
        viewDataBinding.mainToolbar.title = "코코마트"
        viewDataBinding.mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(viewDataBinding.mainToolbar)

        // 권한 요청
        requestPermissions(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        ), 0)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) { // 토큰이 없을 때 = 로그인 정보가 없을 때

            } else if (tokenInfo != null) {
                val app = application as GlobalApplication
                app.kakaoAccountId = tokenInfo.id

                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Kakao login", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        app.kakaoAccountNickname = user.kakaoAccount?.profile?.nickname
                        Log.d("Kakao login","${app.kakaoAccountId}")
                        Log.d("Kakao login","${app.kakaoAccountNickname}")
                    }
                }
            }
        }

        // 바텀 내비게이션의 이벤트 리스너를 설정
        viewDataBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    viewDataBinding.mainToolbar.title = "코코마트"
                    replace(R.id.mainContainer, homeFragment)
                    true
                }
                R.id.action_favorite -> {
                    viewDataBinding.mainToolbar.title = "관심"
                    replace(R.id.mainContainer, favoriteFragment)
                    true
                }
                R.id.action_account -> {
                    val app = application as GlobalApplication

                    if(app.kakaoAccountId != -1L) {
                        viewDataBinding.mainToolbar.title = "계정"
                        replace(R.id.mainContainer, accountFragment)
                    } else{
                        setFragment("login")
                    }
                    true
                }
                else -> false
            }
        }

        replace(R.id.mainContainer, homeFragment)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            0 -> {
                if(resultCode == RESULT_CANCELED){
                    viewDataBinding.bottomNavigation.selectedItemId = R.id.action_home
                }
            }
        }
    }
}