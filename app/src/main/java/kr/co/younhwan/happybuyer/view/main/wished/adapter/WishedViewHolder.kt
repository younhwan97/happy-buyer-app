package kr.co.younhwan.happybuyer.view.main.wished.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.WishedItemBinding

class WishedViewHolder(
    private val parent: ViewGroup,
    wishedItemBinding: WishedItemBinding,
    private val listenerFuncOfDeleteBtn: ((Int, Int) -> Unit)?,
    private val listenerFuncOfBasketBtn: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(wishedItemBinding.root) {

    private val wishedItemImage by lazy {
        wishedItemBinding.wishedItemImage
    }

    private val wishedItemPrice by lazy {
        wishedItemBinding.wishedItemPrice
    }

    private val wishedItemName by lazy {
        wishedItemBinding.wishedItemName
    }

    private val wishedBasketBtn by lazy {
        wishedItemBinding.wishedBasketBtn
    }

    private val wishedDeleteBtn by lazy {
        wishedItemBinding.wishedDeleteBtn
    }

    fun onBind(productItem: ProductItem) {
        wishedItemName.text = productItem.productName
        wishedItemPrice.text = productItem.productPrice.toString()
        Glide.with(this.itemView.context)
            .load(productItem.productImageUrl)
            .error(R.mipmap.ic_launcher)
            .into(wishedItemImage)

        /* set delete/basket button */
        wishedDeleteBtn.isEnabled = true
        wishedBasketBtn.isEnabled = true

        wishedDeleteBtn.setOnClickListener {
            wishedDeleteBtn.isEnabled = false
            listenerFuncOfDeleteBtn?.invoke(productItem.productId, adapterPosition)
        }

        wishedBasketBtn.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId)
        }
    }
}