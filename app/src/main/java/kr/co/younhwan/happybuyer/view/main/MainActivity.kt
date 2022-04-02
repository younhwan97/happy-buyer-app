package kr.co.younhwan.happybuyer.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kr.co.younhwan.happybuyer.view.main.orderhistory.OrderHistoryFragment

class MainActivity : AppCompatActivity(), MainContract.View {
    lateinit var viewDataBinding: ActivityMainBinding

    private val mainPresenter: MainPresenter by lazy {
        MainPresenter(
            this
        )
    }

    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }

    private val wishedFragment: WishedFragment by lazy {
        WishedFragment()
    }

    private val orderHistoryFragment: OrderHistoryFragment by lazy {
        OrderHistoryFragment()
    }

    private val accountFragment: AccountFragment by lazy {
        AccountFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        mainPresenter.requestPermission() // 권한 요청
        replace(R.id.mainContentContainer, homeFragment) // set init fragment

        viewDataBinding.mainToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.basketInMain -> {
                    val basketIntent = Intent(this, BasketActivity::class.java)
                    startActivity(basketIntent)
                    true
                }
                else -> false
            }
        }

        viewDataBinding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeInBottomNav -> {
                    viewDataBinding.mainToolbar.title = "코코마트"
                    replace(R.id.mainContentContainer, homeFragment)
                    true
                }
                R.id.wishedInBottomNav -> {
                    viewDataBinding.mainToolbar.title = "찜한 상품"
                    replace(R.id.mainContentContainer, wishedFragment)
                    true
                }
                R.id.accountInBottomNav -> {
                    if ((application as GlobalApplication).isLogined) { // 로그인 상태
                        viewDataBinding.mainToolbar.title = "계정"
                        replace(R.id.mainContentContainer, accountFragment)
                    } else { // 비로그인 상태
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startForResult.launch(loginIntent)
                    }
                    true
                }
                R.id.orderHistoryInBottomNav -> {
                    viewDataBinding.mainToolbar.title = "주문내역"
                    replace(R.id.mainContentContainer, orderHistoryFragment)
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
                viewDataBinding.mainBottomNavigation.selectedItemId = R.id.homeInBottomNav
        }
}