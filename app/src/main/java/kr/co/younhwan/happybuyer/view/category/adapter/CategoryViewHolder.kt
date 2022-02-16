package kr.co.younhwan.happybuyer.view.category.adapter

import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import kotlin.math.roundToInt

class CategoryViewHolder(
    private val parent: ViewGroup,
    itemBinding: ItemBinding,
    private val listenerFuncHeartBtn: ((Int) -> Unit)?,
    private val listenerFuncShoppingCartBtn: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val itemName by lazy {
        itemBinding.itemName
    }

    private val itemPrice by lazy {
        itemBinding.itemPrice
    }

    private val itemImage by lazy {
        itemBinding.itemImage
    }

    private val itemContainer by lazy {
        itemBinding.itemContainer
    }

    private val heartBtn by lazy {
        itemBinding.heartBtn
    }

    private val shoppingCartBtn by lazy {
        itemBinding.shoppingCartBtn
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

        heartBtn.setOnClickListener {
            listenerFuncHeartBtn?.invoke(productItem.productId)
        }

        shoppingCartBtn.setOnClickListener {
            listenerFuncShoppingCartBtn?.invoke(productItem.productId)
        }
    }
}
