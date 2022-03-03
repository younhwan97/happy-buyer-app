package kr.co.younhwan.happybuyer.view.basket.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
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

    private val itemName by lazy {
        basketItemBinding.basketItemName
    }

    private val itemPrice by lazy {
        basketItemBinding.basketItemPrice
    }

    private val itemImage by lazy {
        basketItemBinding.basketItemImage
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

    private val deleteBtn by lazy {
        basketItemBinding.basketItemDeleteBtn
    }

    private val decimal = DecimalFormat("#,###")

    fun onBind(productItem: ProductItem){

        // 상품 이름
        itemName.text = productItem.productName

        // 상품 가격
        if(productItem.onSale){
            itemPrice.text = decimal.format(productItem.eventPrice * productItem.countInBasket)
        } else {
            itemPrice.text = decimal.format(productItem.productPrice * productItem.countInBasket)
        }

        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(productItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)

        // 상품 개수
        itemCount.text = productItem.countInBasket.toString()

        // 상품 개수 + , -
        plusBtn.isClickable = true
        plusBtn.setOnClickListener {
            if(productItem.countInBasket in 1..9){
                plusBtn.isClickable = false
                listenerFunOfPlusBtn?.invoke(productItem.productId, adapterPosition)
            }
        }

        minusBtn.isClickable = true
        minusBtn.setOnClickListener {
            if(productItem.countInBasket in 2..10){
                minusBtn.isClickable = false
                listenerFunOfMinusBtn?.invoke(productItem.productId, adapterPosition)
            }
        }

        // 삭제 버튼
        deleteBtn.isClickable = true
        deleteBtn.setOnClickListener {
            deleteBtn.isClickable = false
            listenerFunOfDeleteBtn?.invoke(productItem.productId, adapterPosition)
        }
    }

    fun onBindBasketCount(productItem: ProductItem){

        if(!minusBtn.isClickable){
            minusBtn.isClickable = !minusBtn.isClickable
        }

        if(!plusBtn.isClickable){
            plusBtn.isClickable = !plusBtn.isClickable
        }

        itemCount.text = productItem.countInBasket.toString()

        if(productItem.onSale){
            itemPrice.text = decimal.format(productItem.eventPrice * productItem.countInBasket)
        } else {
            itemPrice.text = decimal.format(productItem.productPrice * productItem.countInBasket)
        }
    }
}