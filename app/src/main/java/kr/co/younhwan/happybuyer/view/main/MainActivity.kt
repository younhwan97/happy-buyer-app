package kr.co.younhwan.happybuyer.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.ActivityMainBinding
import kr.co.younhwan.happybuyer.util.replace
import kr.co.younhwan.happybuyer.util.setupBadge
import kr.co.younhwan.happybuyer.view.main.account.AccountFragment
import kr.co.younhwan.happybuyer.view.main.wished.WishedFragment
import kr.co.younhwan.happybuyer.view.main.home.HomeFragment

class MainActivity : AppCompatActivity(), MainContract.View {

    /* View Binding */
    lateinit var viewDataBinding: ActivityMainBinding

    /* Presenter */
    private val mainPresenter: MainPresenter by lazy {
        MainPresenter(
            this
        )
    }

    /* Fragments*/
    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }
    private val wishedFragment: WishedFragment by lazy {
        WishedFragment()
    }
    private val accountFragment: AccountFragment by lazy {
        AccountFragment()
    }

    var textCartItemCount: TextView? = null

    override fun onResume() {
        super.onResume()
        setupBadge(textCartItemCount)
    }

    /* Method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create binding object
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // action bar -> toolbar
        viewDataBinding.mainToolbar.title = "코코마트"
        viewDataBinding.mainToolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTheme)
        setSupportActionBar(viewDataBinding.mainToolbar)

        // presenter
        mainPresenter.requestPermission() // 권한 요청

        // init fragment
        replace(R.id.mainContentContainer, homeFragment)

        // set event listener to bottom nav
        viewDataBinding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    viewDataBinding.mainToolbar.title = "코코마트"
                    replace(R.id.mainContentContainer, homeFragment)
                    true
                }
                R.id.action_wished -> {
                    viewDataBinding.mainToolbar.title = "찜"
                    replace(R.id.mainContentContainer, wishedFragment)
                    true
                }
                R.id.action_account -> {
                    if ((application as GlobalApplication).isLogined) { // 로그인 상태
                        viewDataBinding.mainToolbar.title = "계정"
                        replace(R.id.mainContentContainer, accountFragment)
                    } else { // 비로그인 상태
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startForResult.launch(loginIntent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    // -----------------------------------------------------
    // ---- 툴바 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // 메뉴 객체 생성 및 부착

        val menuItem = menu?.findItem(R.id.basket_item_in_main)
        val actionView = menuItem?.actionView
        textCartItemCount = actionView?.findViewById<TextView>(R.id.cart_badge)

        actionView?.setOnClickListener{
            onOptionsItemSelected(menuItem)
        }

        return true
    }

    // 툴바의 아이템이 클릭됐을 때 발생하는 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.basket_item_in_main -> {
                val basketIntent = Intent(this, BasketActivity::class.java)
                startActivity(basketIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    // 툴바 설정 ----
    // -----------------------------------------------------

    override fun getAct() = this

    /* Activity Result */
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CANCELED)
                viewDataBinding.mainBottomNavigation.selectedItemId = R.id.action_home
        }
}