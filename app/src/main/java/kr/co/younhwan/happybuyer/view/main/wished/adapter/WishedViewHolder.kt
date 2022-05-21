package kr.co.younhwan.happybuyer.view.main.wished.adapter

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerWishedItemBinding
import java.text.DecimalFormat

class WishedViewHolder(
    private val parent: ViewGroup,
    wishedItemBinding: RecyclerWishedItemBinding,
    private val listenerFuncOfProduct: ((ProductItem) -> Unit)?,
    private val listenerFuncOfDeleteBtn: ((Int, Int) -> Unit)?,
    private val listenerFuncOfBasketBtn: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(wishedItemBinding.root) {

    private val itemImage by lazy {
        wishedItemBinding.wishedItemImage
    }

    private val itemName by lazy {
        wishedItemBinding.wishedItemName
    }

    // 상품 가격
    private val itemPrice by lazy {
        wishedItemBinding.wishedItemPrice
    }

    private val itemPriceSubText by lazy {
        wishedItemBinding.wishedItemPriceSubText
    }

    // 상품 행사 가격 및 퍼센트
    private val itemEventPriceContainer by lazy {
        wishedItemBinding.wishedItemEventPriceContainer
    }

    private val itemEventPrice by lazy {
        wishedItemBinding.wishedItemEventPrice
    }

    private val itemEventPriceSubText by lazy {
        wishedItemBinding.wishedItemEventPriceSubText
    }

    private val itemEventPercent by lazy {
        wishedItemBinding.wishedItemEventPercent
    }

    private val itemEventPercentSubText by lazy {
        wishedItemBinding.wishedItemEventPercentSubText
    }

    // 버튼
    private val basketBtn by lazy {
        wishedItemBinding.wishedBasketBtn
    }

    private val deleteBtn by lazy {
        wishedItemBinding.wishedDeleteBtn
    }

    // 가격 표시 방식
    private val decimal = DecimalFormat("#,###")

    fun onBind(productItem: ProductItem) {
        // 상품
        itemView.setOnClickListener {
            listenerFuncOfProduct?.invoke(productItem)
        }

        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(productItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)

        // 상품 이름
        itemName.text = productItem.productName

        // 상품 가격
        itemPrice.text = decimal.format(productItem.productPrice)
        itemPrice.paintFlags = 0
        itemPrice.setTextAppearance(R.style.NumberTextView_Bold)
        itemPrice.textSize = 16F
        itemPriceSubText.text = "원"
        itemPriceSubText.paintFlags = 0
        itemPriceSubText.setTextAppearance(R.style.TextView_Bold)
        itemPriceSubText.textSize = 16F

        itemEventPriceContainer.visibility = View.GONE
        if (productItem.onSale) {
            // 상품 가격
            itemPrice.text = decimal.format(productItem.productPrice)
            itemPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemPrice.setTextAppearance(R.style.NumberTextView_Light)
            itemPrice.textSize = 14F
            itemPriceSubText.text = "원"
            itemPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemPriceSubText.setTextAppearance(R.style.TextView_Light)
            itemPriceSubText.textSize = 14F

            // 행사 가격
            itemEventPrice.text = decimal.format(productItem.eventPrice)
            itemEventPrice.paintFlags = 0
            itemEventPrice.setTextAppearance(R.style.NumberTextView_Bold)
            itemEventPrice.textSize = 16F
            itemEventPriceSubText.text = "원"
            itemEventPriceSubText.paintFlags = 0
            itemEventPriceSubText.setTextAppearance(R.style.TextView_Bold)
            itemEventPriceSubText.textSize = 16F

            // 행사 퍼센트
            itemEventPercent.text =
                (100 - (productItem.productPrice / productItem.eventPrice)).toString()
            itemEventPercent.paintFlags = 0
            itemEventPercent.textSize = 16F
            itemEventPercentSubText.text = "%"
            itemEventPercentSubText.paintFlags = 0
            itemEventPercentSubText.textSize = 16F

            itemEventPriceContainer.visibility = View.VISIBLE
        }

        // 삭제, 장바구니 추가 버튼
        deleteBtn.isEnabled = true
        deleteBtn.setOnClickListener {
            deleteBtn.isEnabled = false
            listenerFuncOfDeleteBtn?.invoke(productItem.productId, adapterPosition)
        }

        basketBtn.isEnabled = true
        basketBtn.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId)
        }
    }
}