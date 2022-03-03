package kr.co.younhwan.happybuyer.view.product

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
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

        // action bar -> toolbar
        setSupportActionBar(viewDataBinding.productToolbar)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }


        productItem = intent.getParcelableExtra("productItem")

        viewDataBinding.run {
            // 컨테이너
            productContentContainer.visibility = View.VISIBLE
            productLoadingContainer.visibility = View.GONE

            // 이미지
            Glide.with(productImage.context).load(productItem!!.productImageUrl).into(productImage)

            // 상품 이름
            productName.text = productItem!!.productName

            // 상품 가격
            productPrice.text = decimal.format(productItem!!.productPrice)
            productPrice.paintFlags = 0
            productPriceSubText.paintFlags = 0
            productEventPriceContainer.visibility = View.GONE

            if(productItem!!.onSale){
                productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPrice.setTextAppearance(R.style.ProductPriceTheme)
                productPriceSubText.setTextAppearance(R.style.ProductPriceTheme)
                // 행사 가격
                productEventPriceContainer.visibility = View.VISIBLE
                productEventPrice.text = decimal.format(productItem!!.eventPrice)
                productEventPercent.text =
                    ((100 - (productItem!!.productPrice / productItem!!.eventPrice)).toString()).plus("%")
            }

            // 찜하기 버튼
            image.isSelected = false

            val app = application as GlobalApplication
            for(item in app.wishedProductId){
                if(productItem!!.productId == item){
                    image.isSelected = true
                    break
                }
            }

            imageButton.setOnClickListener {
                productPresenter.clickWishedBtn(productItem!!.productId)
            }
        }
    }

    /* set menu event listener */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getAct() = this

    override fun createLoginActivity() =
        startActivity(Intent(this, LoginActivity::class.java))

    override fun createProductInWishedResultCallback(productId:Int, perform: String?) {
        val app = application as GlobalApplication
        var snack : Snackbar? = null

        when(perform){
            "error", null -> {

            }

            "create" -> {
                // application 찜 리스트 업데이트
                app.wishedProductId.add(productId)
                
                // 뷰 업데이트
                viewDataBinding.image.isSelected = true
                viewDataBinding.imageButton.likeAnimation()

                // 스낵바 설정
                snack = Snackbar.make(viewDataBinding.root, "관심에 추가되었습니다 :)", Snackbar.LENGTH_SHORT)

            }

            "delete" -> {
                // application 찜 리스트 업데이트
                for(index in 0 until app.wishedProductId.size){
                    if(productId == app.wishedProductId[index]){
                        app.wishedProductId.removeAt(index)
                    }
                }
                
                // 뷰 업데이트
                viewDataBinding.image.isSelected = false
                
                // 스낵바 설정
                snack = Snackbar.make(viewDataBinding.root, "관심에서 제외되었습니다 :(", Snackbar.LENGTH_SHORT)
            }
        }

        snack?.setAnchorView(R.id.productBottomNav)
        snack?.show()
    }
}






