package kr.co.younhwan.happybuyer.adapter.product

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ProductItemBinding


class ProductAdapter(private val usingBy: String?) :
    ProductAdapterContract.View,
    ProductAdapterContract.Model,
    RecyclerView.Adapter<ProductViewHolder>() {

    /* Item */
    private lateinit var productItemList: ArrayList<ProductItem>
    override var onClickFuncOfProduct: ((ProductItem) -> Unit)? = null
    override var onClickFuncOfBasketBtn: ((Int, Int) -> Unit)? = null

    /* Method */
    override fun getItemCount() = this.productItemList.size

    override fun clearItem() = this.productItemList.clear()

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun getItem(position: Int) = this.productItemList[position]

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(productItem = productItemList[position], usingBy = usingBy)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemBinding = ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProductViewHolder(
            parent = parent,
            itemBinding = itemBinding,
            listenerFuncOfProduct = onClickFuncOfProduct,
            listenerFuncOfBasketBtn = onClickFuncOfBasketBtn
        )
    }

    inner class RecyclerDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemPosition = parent.getChildAdapterPosition(view)
            val density = parent.resources.displayMetrics.density

            // 사용되는 엑티비티에 따라 아이템 데코레이션을 조정
            if(usingBy == "home"){
                val outsideSpaceByPx = (16 * density).toInt()
                val insideSpaceByPx = (10 * density).toInt()

                when (itemPosition) {
                    0 -> {
                        outRect.left = outsideSpaceByPx
                        outRect.right = insideSpaceByPx
                    }

                    productItemList.size - 1 -> {
                        outRect.right = outsideSpaceByPx
                    }

                    else -> {
                        outRect.right = insideSpaceByPx
                    }
                }

                outRect.bottom = outsideSpaceByPx
            } else if(usingBy == "category"){
                val insideSpaceByPx = (4 * density).toInt()

                if(itemPosition % 2 != 0){
                    outRect.left = insideSpaceByPx
                } else {
                    outRect.right = insideSpaceByPx
                }

                outRect.bottom = (28 * density).toInt()
            }
        }
    }
}