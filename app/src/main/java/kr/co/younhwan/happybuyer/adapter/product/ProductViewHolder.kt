package kr.co.younhwan.happybuyer.adapter.product

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ProductItemBinding
import java.text.DecimalFormat

class ProductViewHolder(
    private val parent: ViewGroup,
    itemBinding: ProductItemBinding,
    private val listenerFuncOfProduct: ((ProductItem) -> Unit)?,
    private val listenerFuncOfBasketBtn: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val productImage by lazy {
        itemBinding.productItemImage
    }

    private val productImageContainer by lazy {
        itemBinding.productItemImageContainer
    }

    private val productName by lazy {
        itemBinding.productItemName
    }

    private val productPrice by lazy {
        itemBinding.productItemPrice
    }

    private val productPriceSubText by lazy {
        itemBinding.productItemPriceSubText
    }

    private val productEventPrice by lazy {
        itemBinding.productItemEventPrice
    }

    private val productEventPercent by lazy {
        itemBinding.productItemEventPercent
    }

    private val productEvent by lazy {
        itemBinding.productItemEvent
    }

    private val productBasketBtn by lazy {
        itemBinding.productItemBasketBtn
    }

    private val decimal by lazy {
        DecimalFormat("#,###")
    }

    fun onBind(productItem: ProductItem, usingBy: String?) {
        // 이름
        productName.text = productItem.productName

        // 가격
        productPrice.text = decimal.format(productItem.productPrice)
        productPrice.paintFlags = 0
        productPrice.setTextAppearance(R.style.NumberTextView_Bold)
        productPrice.textSize = 14F // 스타일 값에 폰트 크기가 지정되어 있기 대문에 스타일 보다 아래서 지정
        productPriceSubText.text = "원" // 서브 텍스트
        productPriceSubText.paintFlags = 0
        productPriceSubText.setTextAppearance(R.style.TextView_Bold)
        productPriceSubText.textSize = 14F

        productEvent.visibility = View.GONE
        if (productItem.onSale) { // 세일 중인 상품의 경우
            productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            productPrice.setTextAppearance(R.style.NumberTextView_Light)
            productPrice.textSize = 12F
            productPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG // 서브 텍스트
            productPriceSubText.setTextAppearance(R.style.TextView_Light)
            productPriceSubText.textSize = 12F
            // 행사 가격
            productEvent.visibility = View.VISIBLE
            productEventPrice.text = decimal.format(productItem.eventPrice)
            productEventPrice.setTextAppearance(R.style.NumberTextView_Bold)
            productEventPercent.text =
                (100 - productItem.eventPrice * 100 / productItem.productPrice).toString()
        }


        // 장바구니 버튼
        productBasketBtn.isClickable = true
        productBasketBtn.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId)
        }

        // 이미지
        if (usingBy == "home") {
            // 사용되는 엑티비티에 따라 이미지 컨테이너의 크기를 변경
            // 홈 -> 152dp, 194dp / 카테고리 -> match_parent, 224dp (default)
            val density = parent.resources.displayMetrics.density
            productImageContainer.layoutParams = RelativeLayout.LayoutParams(
                (152 * density).toInt(),
                (194 * density).toInt()
            )
        }

        Glide.with(this.itemView.context).load(productItem.productImageUrl).into(productImage)
        productImage.setOnClickListener {
            listenerFuncOfProduct?.invoke(productItem)
        }
    }
}