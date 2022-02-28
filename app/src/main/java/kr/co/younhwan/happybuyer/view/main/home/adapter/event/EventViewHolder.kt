package kr.co.younhwan.happybuyer.view.main.home.adapter.event


import android.graphics.Paint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerEventItemBinding
import java.text.DecimalFormat

class EventViewHolder(
    private val parent: ViewGroup,
    eventItemBinding: RecyclerEventItemBinding,
    private val listenerFuncOfWishedBtn: ((Int, Int) -> Unit)?,
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

    private val wishedBtn by lazy {
        eventItemBinding.eventWishedBtn
    }

    private val wishedBtnContainer by lazy {
        eventItemBinding.eventWishedBtnContainer
    }

    fun onBind(productItem: ProductItem) {
        val decimal = DecimalFormat("#,###")

        itemName.text = productItem.productName
        eventItemPrice.text = decimal.format(productItem.eventPrice)
        originalImagePrice.text = decimal.format(productItem.productPrice)
        originalImagePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        eventPercent.text = ((100 - (productItem.productPrice / productItem.eventPrice)).toString()).plus("%")

        wishedBtn.isActivated = productItem.isWished
        wishedBtnContainer.setOnClickListener {
            listenerFuncOfWishedBtn?.invoke(productItem.productId, adapterPosition)
        }

        Glide.with(this.itemView.context).load(productItem.productImageUrl).into(itemImage)
    }

    fun onBindWishedState(productItem: ProductItem){
        wishedBtn.isActivated = productItem.isWished
    }
}