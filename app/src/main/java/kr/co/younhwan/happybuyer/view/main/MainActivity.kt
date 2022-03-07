package kr.co.younhwan.happybuyer.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityMainBinding
import kr.co.younhwan.happybuyer.util.replace
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create binding object
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // presenter
        mainPresenter.requestPermission() // 권한 요청

        // init fragment
        replace(R.id.mainContentContainer, homeFragment)

        // set event listener to bottom nav
        viewDataBinding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeIconInBottomNav -> {
                    //viewDataBinding.mainTitle.text = "코코마트"
                    replace(R.id.mainContentContainer, homeFragment)
                    true
                }
                R.id.wishedIconInBottomNav -> {
                   // viewDataBinding.mainTitle.text = "찜"
                    replace(R.id.mainContentContainer, wishedFragment)
                    true
                }
                R.id.accountIconInBottomNav -> {
                    if ((application as GlobalApplication).isLogined) { // 로그인 상태
                      //  viewDataBinding.mainTitle.text = "계정"
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

        viewDataBinding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.basketInMain -> {
                    val basketIntent = Intent(this, BasketActivity::class.java)
                    startActivity(basketIntent)
                    true
                }

                else -> false
            }
        }
    }

    override fun getAct() = this

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_CANCELED)
                viewDataBinding.mainBottomNavigation.selectedItemId = R.id.homeIconInBottomNav
        }
}