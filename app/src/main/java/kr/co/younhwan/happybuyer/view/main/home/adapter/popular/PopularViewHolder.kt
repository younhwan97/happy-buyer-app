package kr.co.younhwan.happybuyer.view.main.home.adapter.popular

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerPopularItemBinding
import java.text.DecimalFormat

class PopularViewHolder (
    parent: ViewGroup,
    popularItemBinding: RecyclerPopularItemBinding,
    private val listenerFuncOfWishedBtn: ((Int, Int) -> Unit)?,
) : RecyclerView.ViewHolder(popularItemBinding.root) {

    private val itemName by lazy {
        popularItemBinding.popularItemName
    }

    private val itemImage by lazy {
        popularItemBinding.popularItemImage
    }

    private val itemEventPercent by lazy {
        popularItemBinding.popularItemEventPercent
    }

    private val itemPrice by lazy {
        popularItemBinding.popularItemPrice
    }

    private val wishedBtn by lazy {
        popularItemBinding.popularWishedBtn
    }

    private val wishedBtnContainer by lazy {
        popularItemBinding.popularWishedBtnContainer
    }

    fun onBind(item: ProductItem, position: Int) {
        val decimal = DecimalFormat("#,###")

        itemName.text = item.productName
        Glide.with(this.itemView.context).load(item.productImageUrl).into(itemImage)

        if(item.eventPrice != 0 && item.onSale){
            itemEventPercent.visibility = View.VISIBLE
            itemEventPercent.text = ((100 - (item.productPrice / item.eventPrice)).toString()).plus("%")
            itemPrice.text = decimal.format(item.eventPrice)
        } else {
            itemEventPercent.visibility = View.GONE
            itemPrice.text = decimal.format(item.productPrice)
        }

        wishedBtn.isActivated = item.isWished
        wishedBtnContainer.setOnClickListener {
            listenerFuncOfWishedBtn?.invoke(item.productId, adapterPosition)
        }
    }

    fun onBindWishedState(productItem: ProductItem){
        wishedBtn.isActivated = productItem.isWished
    }
}