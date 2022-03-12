package kr.co.younhwan.happybuyer.view.basket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.ActivityBasketBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.BasketAdapter

class BasketActivity : AppCompatActivity(), BasketContract.View {
    lateinit var viewDataBinding: ActivityBasketBinding

    private val basketAdapter: BasketAdapter by lazy {
        BasketAdapter()
    }

    private val basketPresenter: BasketPresenter by lazy {
        BasketPresenter(
            this,
            productData = ProductRepository,
            adapterModel = basketAdapter,
            adapterView = basketAdapter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        basketPresenter.loadBasketProduct(false)


        viewDataBinding.run {




            basketRecycler.run {
                adapter = basketAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun getAct() = this
}