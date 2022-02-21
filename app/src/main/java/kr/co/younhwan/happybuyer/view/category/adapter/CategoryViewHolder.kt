package kr.co.younhwan.happybuyer.view.category.adapter

import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ProductItemBinding
import kotlin.math.roundToInt

class CategoryViewHolder(
    private val parent: ViewGroup,
    productItemBinding: ProductItemBinding,
    private val listenerFuncOfWishedBtn: ((Int, Int) -> Unit)?,
    private val listenerFuncOfBasketBtn: ((Int, Int) -> Unit)?
) : RecyclerView.ViewHolder(productItemBinding.root) {

    private val itemName by lazy {
        productItemBinding.itemName
    }

    private val itemPrice by lazy {
        productItemBinding.itemPrice
    }

    private val itemImage by lazy {
        productItemBinding.itemImage
    }

    private val itemContainer by lazy {
        productItemBinding.itemContainer
    }

    private val wishedBtn by lazy {
        productItemBinding.wishedBtn
    }

    private val basketBtn by lazy {
        productItemBinding.basketBtn
    }

    private val basketBtnContainer by lazy {
        productItemBinding.basketBtnContainer
    }

    private val wishedBtnContainer by lazy {
        productItemBinding.wishedBtnContainer
    }


    fun onBind(productItem: ProductItem, position: Int) {
        itemName.text = productItem.productName
        itemPrice.text = productItem.productPrice.toString()
        Glide.with(this.itemView.context).load(productItem.productImageUrl).into(itemImage)

        val metrics: DisplayMetrics = parent.resources.displayMetrics
        val outSidePadding = (15 * metrics.density).roundToInt()
        val inSidePadding = (5 * metrics.density).roundToInt()
        val topPadding = (5 * metrics.density).roundToInt()

        if (position % 2 == 0)
            itemContainer.setPadding(outSidePadding, topPadding, inSidePadding, 0)
        else
            itemContainer.setPadding(inSidePadding, topPadding, outSidePadding, 0)


        /* set wished/basket button */
        wishedBtn.isClickable = false
        wishedBtnContainer.isClickable = true
        basketBtn.isClickable = false
        basketBtnContainer.isClickable = true

        wishedBtn.isActivated = productItem.isWished

        wishedBtnContainer.setOnClickListener {
            listenerFuncOfWishedBtn?.invoke(productItem.productId, position)
        }

        basketBtnContainer.setOnClickListener {
            listenerFuncOfBasketBtn?.invoke(productItem.productId, position)
        }
    }

    fun onBindWishedState(productItem: ProductItem, position: Int){
        wishedBtn.isActivated = productItem.isWished
    }
}
