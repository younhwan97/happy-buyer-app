package kr.co.younhwan.happybuyer.view.main.home.adapter.event


import android.graphics.Paint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.EventItemBinding

class EventViewHolder(
    private val parent: ViewGroup,
    eventItemBinding: EventItemBinding
) : RecyclerView.ViewHolder(eventItemBinding.root) {


    private val itemImage by lazy {
        eventItemBinding.eventItemImage
    }

    private val itemName by lazy {
        eventItemBinding.eventItemName
    }

    private val originalImagePrice by lazy {
        eventItemBinding.eventItemOriginalPrice
    }

    private val eventItemPrice by lazy {
        eventItemBinding.eventItemPrice
    }

    private val eventPercent by lazy {
        eventItemBinding.eventPercent
    }

    fun onBind(productItem: ProductItem) {
        itemName.text = productItem.productName
        originalImagePrice.text = productItem.productPrice.toString()
        originalImagePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        eventPercent.text = (100 - (productItem.productPrice / productItem.eventPrice)).toString() + "%"

        eventItemPrice.text = productItem.eventPrice.toString()


        Glide.with(this.itemView.context).load(productItem.productImageUrl).into(itemImage)
    }
}