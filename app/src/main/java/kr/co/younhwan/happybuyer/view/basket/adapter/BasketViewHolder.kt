package kr.co.younhwan.happybuyer.view.basket.adapter

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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
    private val listenerFunOfCheckBox: ((Int, Boolean) -> Unit)?,
    private val listenerFunOfPlusBtn: ((BasketItem, Int) -> Unit)?,
    private val listenerFunOfMinusBtn: ((BasketItem, Int) -> Unit)?,
    private val listenerFunOfDeleteBtn: ((BasketItem, Int) -> Unit)?
) : RecyclerView.ViewHolder(basketItemBinding.root){

    private val decimal = DecimalFormat("#,###")

    private val itemCheckBox by lazy {
        basketItemBinding.basketItemCheckBox
    }

    private val itemName by lazy {
        basketItemBinding.basketItemName
    }

    private val itemImage by lazy {
        basketItemBinding.basketItemImage
    }

    // 상품 가격 및 서브 텍스트
    private val itemPrice by lazy {
        basketItemBinding.basketItemPrice
    }

    private val itemPriceSubText by lazy {
        basketItemBinding.basketItemPriceSubText
    }

    // 상품 행사 가격 및 서브 텍스트
    private val itemEventPrice by lazy {
        basketItemBinding.basketItemEventPrice
    }

    private val itemEventPriceSubText by lazy {
        basketItemBinding.basketItemEventPriceSubText
    }

    // 상품 품절 아이콘 및 텍스트 컨테이너
    private val itemDisturbContainer by lazy {
        basketItemBinding.basketItemDisturbContainer
    }

    // 상품 개수 및 증감 버튼
    private val itemCount by lazy {
        basketItemBinding.basketItemCount
    }
    
    private val itemCountPlusBtn by lazy {
        basketItemBinding.basketItemPlusBtn
    }

    private val itemCountMinusBtn by lazy {
        basketItemBinding.basketItemMinusBtn
    }

    // 상품 삭제 버튼
    private val itemDeleteBtn by lazy {
        basketItemBinding.basketItemDeleteBtn
    }

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

        itemCheckBox.setOnClickListener {
            listenerFunOfCheckBox?.invoke(basketItem.productId, !basketItem.isChecked)
        }

        // 상품 이름
        itemName.text = basketItem.productName

        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(basketItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)
        
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
        if(basketItem.onSale){ // 상품이 행사 중일 때
            itemPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG // 기존 상품 가격
            itemPriceSubText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemPrice.setTextAppearance(R.style.NumberTextView_Light)
            itemPriceSubText.setTextAppearance(R.style.TextView_Light)
            itemPrice.textSize = 14F
            itemPriceSubText.textSize = 14F
            itemEventPrice.visibility = View.VISIBLE // 행사 상품 가격
            itemEventPriceSubText.visibility = View.VISIBLE
            itemEventPrice.text = decimal.format(basketItem.eventPrice * basketItem.countInBasket)
            itemEventPrice.setTextAppearance(R.style.NumberTextView_Bold)
            itemEventPrice.textSize = 16F
            itemEventPriceSubText.setTextAppearance(R.style.TextView_Bold)
            itemEventPriceSubText.textSize = 16F
        }

        itemDisturbContainer.visibility = View.GONE
        if(basketItem.productStatus == "품절"){ // 상품이 품절 상태 일 때
            itemPrice.paintFlags = 0 // 기존 상품 가격
            itemPriceSubText.paintFlags = 0
            itemPrice.setTextAppearance(R.style.NumberTextView_Light)
            itemPriceSubText.setTextAppearance(R.style.TextView_Light)
            itemPrice.textSize = 16F
            itemPriceSubText.textSize = 16F
            itemEventPrice.visibility = View.GONE // 행사 상품 가격
            itemEventPriceSubText.visibility = View.GONE
            itemDisturbContainer.visibility = View.VISIBLE // 품절 표시
        }
        
        // 상품 개수 및 증감 버튼
        itemCount.text = basketItem.countInBasket.toString() // 상품 개수
        itemCount.setTextAppearance(R.style.NumberTextView)
        
        itemCountPlusBtn.isEnabled = true // 상품 개수 증감버튼
        itemCountMinusBtn.isEnabled = true
        if(basketItem.productStatus == "품절"){
            itemCount.setTextAppearance(R.style.NumberTextView_Light)
            itemCountPlusBtn.isEnabled = false
            itemCountMinusBtn.isEnabled = false
        }

        if(basketItem.countInBasket == 20){
            itemCountPlusBtn.isEnabled = false
        }

        if(basketItem.countInBasket == 1){
            itemCountMinusBtn.isEnabled = false
        }

        itemCountPlusBtn.isClickable = true
        itemCountPlusBtn.setOnClickListener {
            itemCountPlusBtn.isClickable = false
            listenerFunOfPlusBtn?.invoke(basketItem, absoluteAdapterPosition)
        }

        itemCountMinusBtn.isClickable = true
        itemCountMinusBtn.setOnClickListener {
            itemCountMinusBtn.isClickable = false
            listenerFunOfMinusBtn?.invoke(basketItem, absoluteAdapterPosition)
        }

        // 상품 삭제 버튼
        itemDeleteBtn.setOnClickListener {
            listenerFunOfDeleteBtn?.invoke(basketItem, absoluteAdapterPosition)
        }
    }

    fun onBindCount(basketItem: BasketItem){
        itemCount.text = basketItem.countInBasket.toString()
        itemPrice.text = decimal.format(basketItem.productPrice * basketItem.countInBasket)

        if(basketItem.onSale){
            itemEventPrice.text = decimal.format(basketItem.eventPrice * basketItem.countInBasket)
        }

        itemCountPlusBtn.isClickable = true
        itemCountPlusBtn.isEnabled = true
        if(basketItem.countInBasket == 20){
            itemCountPlusBtn.isEnabled = false
        }

        itemCountMinusBtn.isClickable = true
        itemCountMinusBtn.isEnabled = true
        if(basketItem.countInBasket == 1){
            itemCountMinusBtn.isEnabled = false
        }
    }
}