package kr.co.younhwan.happybuyer.view.basket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.ActivityBasketBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.BasketAdapter
import java.text.DecimalFormat

class BasketActivity : AppCompatActivity(), BasketContract.View {
    lateinit var viewDataBinding: ActivityBasketBinding

    private val basketAdapter: BasketAdapter by lazy {
        BasketAdapter()
    }

    private val basketPresenter: BasketPresenter by lazy {
        BasketPresenter(
            this,
            productData = ProductRepository,
            basketAdapterModel = basketAdapter,
            basketAdapterView = basketAdapter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        basketPresenter.loadBasketProduct(false)

        // 툴바
        viewDataBinding.basketToolbar.setNavigationOnClickListener {
            finish()
        }

        // 탑 컨테이너
        viewDataBinding.basketChechBox.isChecked = true

        viewDataBinding.basketChechBox.setOnClickListener {
            basketPresenter.checkAllBasketProduct()
        }

        viewDataBinding.basketCheckBoxText.setOnClickListener {
            viewDataBinding.basketChechBox.performClick()
        }

        viewDataBinding.basketDeleteBtn.setOnClickListener {

        }

        // 장바구니
        viewDataBinding.basketRecycler.adapter = basketAdapter
        viewDataBinding.basketRecycler.layoutManager = LinearLayoutManager(this)
        viewDataBinding.basketRecycler.addItemDecoration(basketAdapter.RecyclerDecoration())
    }

    override fun getAct() = this

    override fun loadBasketProductCallback(size: Int, totalPrice:Int) {
        if(size != 0){
            val decimal = DecimalFormat("#,###")
            viewDataBinding.basketPurchaseBtn.text = decimal.format(totalPrice).plus("원 주문하기")
        }
    }
}