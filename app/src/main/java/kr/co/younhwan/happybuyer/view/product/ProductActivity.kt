package kr.co.younhwan.happybuyer.view.product

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.ActivityProductBinding
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import java.text.DecimalFormat

class ProductActivity : AppCompatActivity(), ProductContract.View {
    /* View Binding */
    lateinit var viewDataBinding: ActivityProductBinding

    /* Presenter */
    private val productPresenter: ProductPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        ProductPresenter(
            view = this,
            productData = ProductRepository
        )
    }

    /* Data */
    var productItem: ProductItem? = null
    private val decimal = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        productItem = intent.getParcelableExtra("productItem")
        if(productItem == null) finish()

        viewDataBinding.run {
            // Bottom Sheet
            val bottomSheetBehavior = BottomSheetBehavior.from(productBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN // 처음 화면을 켰을 때 시트가 닫혀있도록 설정
            bottomSheetCloseBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            bottomSheetProductCount.text = "1"
            bottomSheetPlusBtn.setOnClickListener {
                var count = (bottomSheetProductCount.text).toString().toInt()

                if (count < 20) {
                    count = count.plus(1)
                    bottomSheetProductCount.text = count.toString()

                    if (productItem!!.onSale) {
                        (productItem!!.eventPrice) * (count)
                    } else {
                        (productItem!!.productPrice) * (count)
                    }.apply {
                        bottomSheetProductTotalPrice.text = decimal.format(this)
                        bottomSheetBtn.text = decimal.format(this).plus("원 장바구니 담기")
                    }
                }
            }

            bottomSheetMinusBtn.setOnClickListener {
                var count = (bottomSheetProductCount.text).toString().toInt()

                if (count > 1) {
                    count = count.minus(1)
                    bottomSheetProductCount.text = count.toString()

                    if (productItem!!.onSale) {
                        (productItem!!.eventPrice) * (count)
                    } else {
                        (productItem!!.productPrice) * (count)
                    }.apply {
                        bottomSheetProductTotalPrice.text = decimal.format(this)
                        bottomSheetBtn.text = decimal.format(this).plus("원 장바구니 담기")
                    }
                }
            }

            bottomSheetBtn.setOnClickListener {
                val count = (bottomSheetProductCount.text).toString().toInt()

                val app = application as GlobalApplication
                productPresenter.createProductInBasket(
                    app.kakaoAccountId,
                    productItem!!.productId,
                    count
                )
            }

            // 상품 이미지
            Glide.with(productImage.context).load(productItem!!.productImageUrl).into(productImage)

            // 상품 이름
            productName.text = productItem!!.productName
            bottomSheetProductName.text = productItem!!.productName // bottom sheet

            // 상품 가격
            productPrice.text = decimal.format(productItem!!.productPrice)
            productPrice.paintFlags = 0
            productPriceSubText.paintFlags = 0
            productEventPriceContainer.visibility = View.GONE
            bottomSheetProductTotalPrice.text =
                decimal.format(productItem!!.productPrice) // bottom sheet
            bottomSheetBtn.text =
                decimal.format(productItem!!.productPrice).plus("원 장바구니 담기") // bottom sheet

            if (productItem!!.onSale) {
                productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPrice.setTextAppearance(R.style.ProductPriceTheme)
                productPriceSubText.setTextAppearance(R.style.ProductPriceTheme)
                // 행사 가격
                productEventPriceContainer.visibility = View.VISIBLE
                productEventPrice.text = decimal.format(productItem!!.eventPrice)
                productEventPercent.text =
                    ((100 - (productItem!!.productPrice / productItem!!.eventPrice)).toString()).plus(
                        "%"
                    )
                bottomSheetProductTotalPrice.text =
                    decimal.format(productItem!!.eventPrice) // bottom sheet
                bottomSheetBtn.text =
                    decimal.format(productItem!!.eventPrice).plus("원 장바구니 담기") // bottom sheet
            }

            // 찜하기 버튼
            image.isSelected = false

            val app = application as GlobalApplication
            for (item in app.wishedProductId) {
                if (productItem!!.productId == item) {
                    image.isSelected = true
                    break
                }
            }

            productWishedBtn.setOnClickListener {
                productPresenter.clickWishedBtn(productItem!!.productId)
            }

            // 구매하기 버튼
            productPurchaseBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun getAct() = this

    override fun createLoginActivity() =
        startActivity(Intent(this, LoginActivity::class.java))

    override fun createProductInWishedResultCallback(productId: Int, perform: String?) {
        val app = application as GlobalApplication

        when (perform) {
            "create" -> {
                // application 찜 리스트 업데이트
                app.wishedProductId.add(productId)

                // 뷰 업데이트
                viewDataBinding.image.isSelected = true
                viewDataBinding.productWishedBtn.likeAnimation()

                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "상품을 찜 목록에 추가했습니다 :)",
                    Snackbar.LENGTH_SHORT
                )
            }

            "delete" -> {
                // application 찜 리스트 업데이트
                for (item in app.wishedProductId) {
                    if (productId == item) {
                        app.wishedProductId.remove(item)
                        break // break 빼먹으면 out ouf index 에러 발생
                    }
                }

                // 뷰 업데이트
                viewDataBinding.image.isSelected = false

                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "상품을 찜 목록에서 제외했습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }
        }.apply {
            setAnchorView(R.id.productBottomNav)
            show()
        }
    }

    override fun createProductInBasketResultCallback(count: Int) {
        when(count){
            0 -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }

            1 -> {
                Snackbar.make(viewDataBinding.root, "장바구니에 상품을 담았습니다.", Snackbar.LENGTH_SHORT)
            }

            20 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "같은 종류의 상품은 최대 20개까지 담을 수 있습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "한 번 더 담으셨네요! \n담긴 수량이 ${count}개가 되었습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }
        }.apply {
            show()
        }
    }
}






