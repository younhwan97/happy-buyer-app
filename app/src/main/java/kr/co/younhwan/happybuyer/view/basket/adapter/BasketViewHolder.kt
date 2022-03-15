package kr.co.younhwan.happybuyer.view.basket.adapter

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.ProductItem
import java.text.DecimalFormat
import kr.co.younhwan.happybuyer.databinding.RecyclerBasketItemBinding

class BasketViewHolder(
    private val parent: ViewGroup,
    basketItemBinding: RecyclerBasketItemBinding,
    private val listenerFunOfPlusBtn: ((Int, Int) -> Unit)?,
    private val listenerFunOfMinusBtn: ((Int, Int) -> Unit)?,
    private val listenerFunOfDeleteBtn: ((Int, Int) -> Unit)?
) : RecyclerView.ViewHolder(basketItemBinding.root){

    private val itemCheckBox by lazy {
        basketItemBinding.basketItemCheckBox
    }

    private val itemName by lazy {
        basketItemBinding.basketItemName
    }

    private val itemPrice by lazy {
        basketItemBinding.basketItemPrice
    }

    private val itemPriceSubText by lazy {
        basketItemBinding.basketItemPriceSubText
    }

    private val itemEventPrice by lazy {
        basketItemBinding.basketItemEventPrice
    }

    private val itemEventPriceSubText by lazy {
        basketItemBinding.basketItemEventPriceSubText
    }

    private val itemImage by lazy {
        basketItemBinding.basketItemImage
    }

    private val itemDeleteBtn by lazy {
        basketItemBinding.basketItemDeleteBtn
    }

    private val itemCount by lazy {
        basketItemBinding.basketItemCount
    }

    private val plusBtn by lazy {
       basketItemBinding.basketItemPlusBtn
    }

    private val minusBtn by lazy {
        basketItemBinding.basketItemMinusBtn
    }

    private val decimal = DecimalFormat("#,###")

    fun onBind(basketItem: BasketItem){
        // 체크 박스
        itemCheckBox.isChecked = basketItem.isChecked
        itemCheckBox.isClickable = true
        itemCheckBox.isEnabled = true
        if(basketItem.productStatus == "품절"){ // 품절 상품
            itemCheckBox.isChecked = true
            itemCheckBox.isClickable = false
            itemCheckBox.isEnabled = false
        }

        // 상품 이름
        itemName.text = basketItem.productName

        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(basketItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)

        // 상품 개수
        itemCount.text = basketItem.countInBasket.toString()

        // 상품 가격
        itemPrice.text = decimal.format(basketItem.productPrice * basketItem.countInBasket)
        itemPrice.paintFlags = 0
        itemPriceSubText.paintFlags = 0
        itemPrice.setTextAppearance(R.style.NumberTextView_Bold) // 로보토 볼드 폰트
        itemPriceSubText.setTextAppearance(R.style.TextView_Bold) // 노토산스 볼드 폰트
        itemPrice.textSize = 16F
        itemPriceSubText.textSize = 16F
        itemEventPrice.visibility = View.GONE
        itemEventPriceSubText.visibility = View.GONE

        if(basketItem.onSale){ // 상품이 세일 중일 때
            itemPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG // 기존 상품 가격
            itemPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemPrice.setTextAppearance(R.style.NumberTextView_Light)
            itemPriceSubText.setTextAppearance(R.style.TextView_Light)
            itemPrice.textSize = 14F
            itemPriceSubText.textSize = 14F
            itemEventPrice.visibility = View.VISIBLE // 세일 상품 가격
            itemEventPriceSubText.visibility = View.VISIBLE
            itemEventPrice.text = decimal.format(basketItem.eventPrice * basketItem.countInBasket)
            itemEventPrice.setTextAppearance(R.style.NumberTextView_Bold)
            itemEventPrice.textSize = 16F
            itemEventPriceSubText.setTextAppearance(R.style.TextView_Bold)
            itemEventPriceSubText.textSize = 16F
        }

//
//        // 상품 개수 + , -
//        plusBtn.isClickable = true
//        plusBtn.setOnClickListener {
//            if(productItem.countInBasket in 1..9){
//                plusBtn.isClickable = false
//                listenerFunOfPlusBtn?.invoke(productItem.productId, adapterPosition)
//            }
//        }
//
//        minusBtn.isClickable = true
//        minusBtn.setOnClickListener {
//            if(productItem.countInBasket in 2..10){
//                minusBtn.isClickable = false
//                listenerFunOfMinusBtn?.invoke(productItem.productId, adapterPosition)
//            }
//        }
//
//        // 삭제 버튼
//        deleteBtn.isClickable = true
//        deleteBtn.setOnClickListener {
//            deleteBtn.isClickable = false
//            listenerFunOfDeleteBtn?.invoke(productItem.productId, adapterPosition)
//        }
    }

    fun onBindBasketCount(productItem: ProductItem){

//        if(!minusBtn.isClickable){
//            minusBtn.isClickable = !minusBtn.isClickable
//        }
//
//        if(!plusBtn.isClickable){
//            plusBtn.isClickable = !plusBtn.isClickable
//        }
//
//        itemCount.text = productItem.countInBasket.toString()
//
//        if(productItem.onSale){
//            itemPrice.text = decimal.format(productItem.eventPrice * productItem.countInBasket)
//        } else {
//            itemPrice.text = decimal.format(productItem.productPrice * productItem.countInBasket)
//        }
    }
}