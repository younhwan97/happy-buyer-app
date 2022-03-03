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
    private val listenerFuncOfBasketBtn: ((Int, Int) -> Unit)?
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

    fun onBind(productItem: ProductItem, usingBy: String?){
        // 이름
        productName.text = productItem.productName
        
        // 가격
        productPrice.text = decimal.format(productItem.productPrice).plus("원")
        productPrice.paintFlags = 0
        productEvent.visibility = View.GONE

        if(productItem.onSale){
            productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            productPrice.setTextAppearance(R.style.ProductPriceTheme)
            // 행사 가격
            productEvent.visibility = View.VISIBLE
            productEventPrice.text = decimal.format(productItem.eventPrice).plus("원")
            productEventPercent.text =
                ((100 - (productItem.productPrice / productItem.eventPrice)).toString()).plus("%")
        }

        // 장바구니 버튼
        productBasketBtn.isClickable = true
        productBasketBtn.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId, adapterPosition)
        }

        // 이미지
        if(usingBy == "home"){
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