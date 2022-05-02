package kr.co.younhwan.happybuyer.view.basket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.databinding.ActivityBasketBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.BasketAdapter
import kr.co.younhwan.happybuyer.view.order.OrderActivity
import java.text.DecimalFormat

class BasketActivity : AppCompatActivity(), BasketContract.View {
    lateinit var viewDataBinding: ActivityBasketBinding

    private val basketPresenter: BasketPresenter by lazy {
        BasketPresenter(
            view = this,
            basketData = BasketRepository,
            basketAdapterModel = basketAdapter,
            basketAdapterView = basketAdapter
        )
    }

    private val basketAdapter: BasketAdapter by lazy {
        BasketAdapter()
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
            basketPresenter.checkAllBasketProduct(viewDataBinding.basketChechBox.isChecked)
        }

        viewDataBinding.basketCheckBoxText.setOnClickListener {
            viewDataBinding.basketChechBox.performClick()
        }

        viewDataBinding.basketDeleteBtn.setOnClickListener {
            basketPresenter.deleteSelectedItems()
        }

        // 장바구니
        viewDataBinding.basketRecycler.adapter = basketAdapter
        viewDataBinding.basketRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.basketRecycler.addItemDecoration(basketAdapter.RecyclerDecoration())

        // 주문하기 버튼
        viewDataBinding.basketPurchaseBtn.setOnClickListener {
            basketPresenter.createOrderAct()
        }
    }

    override fun getAct() = this

    override fun onClickCheckBoxCallback(isCheckedAllBasketItem: Boolean) {
        viewDataBinding.basketChechBox.isChecked = isCheckedAllBasketItem
    }

    override fun calculatePriceCallback(
        totalPrice: Int,
        originalTotalPrice: Int,
        basketItemCount: Int
    ) {
        val decimal = DecimalFormat("#,###")
        viewDataBinding.basketOriginalPriceText.text = decimal.format(originalTotalPrice)
        viewDataBinding.basketEventPriceText.text = decimal.format(totalPrice - originalTotalPrice)
        viewDataBinding.basketBePaidPriceText.text = decimal.format(totalPrice)

        viewDataBinding.basketTopContainer.visibility = View.VISIBLE
        viewDataBinding.basketContentContainer.visibility = View.VISIBLE
        when (totalPrice) {
            in 0..49999 -> {
                if (basketItemCount == 0) {
                    // 장바구니에 상품이 아무것도 없어서 총 금액이 0원인 경우
                    viewDataBinding.basketPurchaseBtn.text = "상품을 담아주세요"

                    viewDataBinding.basketTopContainer.visibility = View.GONE
                    viewDataBinding.basketContentContainer.visibility = View.GONE
                    viewDataBinding.basketEmptyContainer.visibility = View.VISIBLE
                } else {
                    // 장바구니에 상품이 존재하나 선택된 상품 금액의 총합이 5만원 미만인 경우
                    viewDataBinding.basketPurchaseBtn.text = "5만원부터 주문할 수 있어요"
                }

                viewDataBinding.basketPurchaseBtn.isEnabled = false
                viewDataBinding.basketPurchaseBtn.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }

            else -> {
                viewDataBinding.basketPurchaseBtn.text = decimal.format(totalPrice).plus("원 주문하기")
                viewDataBinding.basketPurchaseBtn.isEnabled = true
                viewDataBinding.basketPurchaseBtn.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.fontColorBlack
                    )
                )
            }
        }
    }

    override fun createOrderActCallback(
        passValidationCheck: Boolean,
        selectedBasketItem: ArrayList<BasketItem>
    ) {
        if (passValidationCheck) {
            val orderIntent = Intent(this, OrderActivity::class.java)
            orderIntent.putExtra("selected_item_list", selectedBasketItem)
            startActivity(orderIntent)
        } else {

        }
    }
}