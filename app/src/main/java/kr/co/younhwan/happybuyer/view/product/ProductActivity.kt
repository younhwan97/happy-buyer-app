package kr.co.younhwan.happybuyer.view.product

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.databinding.ActivityProductBinding
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.text.DecimalFormat

class ProductActivity : AppCompatActivity(), ProductContract.View {
    lateinit var viewDataBinding: ActivityProductBinding

    private val productPresenter: ProductPresenter by lazy {
        ProductPresenter(
            view = this,
            productData = ProductRepository,
            wishedData = WishedRepository
        )
    }

    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 인텐트에서 데이터 추출
        val product = intent.getParcelableExtra<ProductItem>("product")

        if (product == null) {
            finish()
        } else {
            val decimal = DecimalFormat("#,###")

            // 툴바
            viewDataBinding.productToolbar.setNavigationOnClickListener {
                finish()
            }

            // 스크롤 컨테이너
            OverScrollDecoratorHelper.setUpOverScroll(viewDataBinding.productContentContainer)

            // 상품 이미지
            Glide.with(viewDataBinding.productImage.context)
                .load(product.productImageUrl)
                .into(viewDataBinding.productImage)

            // 상품 이름
            viewDataBinding.productName.text = product.productName

            // 상품 가격
            viewDataBinding.productPrice.text = decimal.format(product.productPrice)
            if (product.onSale) {
                // 상품이 행사중 일 때
                viewDataBinding.productEventPriceContainer.visibility = View.VISIBLE
                viewDataBinding.productEventPrice.text = decimal.format(product.eventPrice)
                viewDataBinding.productEventPercent.text =
                    ((100 - (product.productPrice / product.eventPrice)).toString())

                viewDataBinding.productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                viewDataBinding.productPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // 상품이 행사중이 아닐 때
                viewDataBinding.productEventPriceContainer.visibility = View.GONE

                viewDataBinding.productPrice.setTextAppearance(R.style.NumberTextView_Bold)
                viewDataBinding.productPrice.textSize = 24F
                viewDataBinding.productPrice.typeface = Typeface.DEFAULT_BOLD
                viewDataBinding.productPriceSubText.setTextAppearance(R.style.TextView)
                viewDataBinding.productPriceSubText.textSize = 16F
                viewDataBinding.productPriceSubText.typeface = Typeface.DEFAULT_BOLD
            }

            // 판매단위

            // 중량/용량

            // 상품 설명

            // 바텀 시트
            bottomSheetBehavior = BottomSheetBehavior.from(viewDataBinding.productBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            // 구매 버튼
            viewDataBinding.productPurchaseBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            // (바텀 시트) 닫기 버튼
            viewDataBinding.productBottomSheetCloseBtnContainer.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            viewDataBinding.productBottomSheetCloseBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            // (바텀 시트) 상품 이름
            viewDataBinding.productBottomSheetName.text = product.productName

            // (바텀 시트) 상품 개수 및 개수 조절 버튼
            viewDataBinding.productBottomSheetCount.text = "1"
            viewDataBinding.productBottomSheetPlusBtn.setOnClickListener {
                var count = (viewDataBinding.productBottomSheetCount.text).toString().toInt()

                if (count < 20) {
                    count = count.plus(1)
                    viewDataBinding.productBottomSheetCount.text = count.toString()

                    val calculatedPrice = if (product.onSale) {
                        (product.eventPrice) * (count)
                    } else {
                        (product.productPrice) * (count)
                    }

                    viewDataBinding.productBottomSheetPrice.text = decimal.format(calculatedPrice)
                    viewDataBinding.productBottomSheetBtn.text =
                        decimal.format(calculatedPrice).plus("원 장바구니 담기")
                }
            }

            viewDataBinding.productBottomSheetMinusBtn.setOnClickListener {
                var count = (viewDataBinding.productBottomSheetCount.text).toString().toInt()

                if (count > 1) {
                    count = count.minus(1)
                    viewDataBinding.productBottomSheetCount.text = count.toString()

                    val calculatedPrice = if (product.onSale) {
                        (product.eventPrice) * (count)
                    } else {
                        (product.productPrice) * (count)
                    }

                    viewDataBinding.productBottomSheetPrice.text = decimal.format(calculatedPrice)
                    viewDataBinding.productBottomSheetBtn.text =
                        decimal.format(calculatedPrice).plus("원 장바구니 담기")
                }
            }

            // (바텀 시트) 상품 가격 및 장바구니 담기 버튼
            if (product.onSale) {
                viewDataBinding.productBottomSheetPrice.text = decimal.format(product.eventPrice)
                viewDataBinding.productBottomSheetBtn.text =
                    decimal.format(product.eventPrice).plus("원 장바구니 담기")
            } else {
                viewDataBinding.productBottomSheetPrice.text = decimal.format(product.productPrice)
                viewDataBinding.productBottomSheetBtn.text =
                    decimal.format(product.productPrice).plus("원 장바구니 담기")
            }
        }
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            super.onBackPressed()
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
                // viewDataBinding.image.isSelected = true
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
                //viewDataBinding.image.isSelected = false

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
            setAnchorView(R.id.productBottomBtnContainer)
            show()
        }
    }

    override fun createProductInBasketResultCallback(count: Int) {
        when (count) {
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






