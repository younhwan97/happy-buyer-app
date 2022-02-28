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
    private val listenerFuncOfDeleteBtn: ((Int, Int) -> Unit)?,
    private val listenerFuncOfBasketBtn: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(wishedItemBinding.root) {

    private val itemImage by lazy {
        wishedItemBinding.wishedItemImage
    }

    private val itemPrice by lazy {
        wishedItemBinding.wishedItemPrice
    }

    private val itemName by lazy {
        wishedItemBinding.wishedItemName
    }

    private val basketBtn by lazy {
        wishedItemBinding.wishedBasketBtn
    }

    private val deleteBtn by lazy {
        wishedItemBinding.wishedDeleteBtn
    }

    private val itemEventPriceContainer by lazy {
        wishedItemBinding.wishedItemEventPriceContainer
    }

    private val itemEventPrice by lazy {
        wishedItemBinding.wishedItemEventPrice
    }

    private val itemEventPercent by lazy {
        wishedItemBinding.wishedItemEventPercent
    }

    private val decimal = DecimalFormat("#,###")

    fun onBind(productItem: ProductItem) {
        // 상품 이름
        itemName.text = productItem.productName

        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(productItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)

        // 상품 가격 (이벤트 상품일 경우 할인률과 할인 가격도 표시)
        itemEventPriceContainer.visibility = View.GONE
        itemPrice.text = decimal.format(productItem.productPrice)
        itemPrice.paintFlags = 0

        if(productItem.onSale){
            itemEventPriceContainer.visibility = View.VISIBLE
            itemEventPrice.text = decimal.format(productItem.eventPrice)
            itemPrice.text = decimal.format(productItem.productPrice)
            itemPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemEventPercent.text = ((100 - (productItem.productPrice / productItem.eventPrice)).toString()).plus("%")
        }

        // 장바구니 추가, 찜 삭제 버튼
        deleteBtn.isEnabled = true
        basketBtn.isEnabled = true

        deleteBtn.setOnClickListener {
            deleteBtn.isEnabled = false
            listenerFuncOfDeleteBtn?.invoke(productItem.productId, adapterPosition)
        }

        basketBtn.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId)
        }
    }
}