package kr.co.younhwan.happybuyer.view.order.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderProductItemBinding
import java.text.DecimalFormat

class OrderViewHolder(
    private val parent: ViewGroup,
    orderItemBinding: RecyclerOrderProductItemBinding
) : RecyclerView.ViewHolder(orderItemBinding.root) {

    private val decimal = DecimalFormat("#,###")

    private val itemName by lazy {
        orderItemBinding.orderProductItemName
    }

    private val itemImage by lazy {
        orderItemBinding.orderProductItemImage
    }

    private val itemPrice by lazy {
        orderItemBinding.orderProductItemPrice
    }

    private val itemCount by lazy {
        orderItemBinding.orderProductItemCount
    }

    fun onBind(basketItem: BasketItem) {
        // 상품 이미지
        Glide.with(this.itemView.context)
            .load(basketItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(itemImage)

        // 상품 이름
        itemName.text = basketItem.productName

        // 상품 가격
        itemPrice.text = decimal.format(basketItem.productPrice * basketItem.countInBasket)
        if(basketItem.onSale){
            itemPrice.text = decimal.format(basketItem.eventPrice * basketItem.countInBasket)
        }

        // 상품 개수
        itemCount.text = basketItem.countInBasket.toString()
    }
}