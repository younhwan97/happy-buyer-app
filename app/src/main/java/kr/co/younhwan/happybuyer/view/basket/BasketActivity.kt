package kr.co.younhwan.happybuyer.view.basket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.ActivityBasketBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.BasketAdapter
import kr.co.younhwan.happybuyer.view.category.adapter.CategoryAdapter
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class BasketActivity : AppCompatActivity(), BasketContract.View {
    /* View Binding */
    lateinit var viewDataBinding: ActivityBasketBinding

    /* Presenter */
    private val basketPresenter: BasketPresenter by lazy {
        BasketPresenter(
            this,
            productData = ProductRepository,
            adapterModel = basketAdapter,
            adapterView = basketAdapter
        )
    }

    private val basketAdapter: BasketAdapter by lazy {
        BasketAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create binding object
        viewDataBinding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // action -> tool bar
        setSupportActionBar(viewDataBinding.basketToolbar)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        basketPresenter.loadBasketProduct(false)

        viewDataBinding.basketRecycler.run{
            this.adapter = basketAdapter
            this.layoutManager = LinearLayoutManager(context)
        }
    }

    /* create menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.basket_menu, menu) // 메뉴 객체 생성 및 부착
        return true
    }

    /* set menu event listener */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.searchIconInBasketMenu -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            R.id.homeIconInBasketMenu -> {
                val splashIntent = Intent(this, MainActivity::class.java)
                splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(splashIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getAct() = this
}