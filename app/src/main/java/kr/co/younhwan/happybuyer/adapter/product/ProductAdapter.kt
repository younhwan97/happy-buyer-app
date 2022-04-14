package kr.co.younhwan.happybuyer.adapter.product

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ProductItemBinding
import kr.co.younhwan.happybuyer.databinding.RecyclerProductLoadingItemBinding

class ProductAdapter(private val usingBy: String?) :
    ProductAdapterContract.View,
    ProductAdapterContract.Model,
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 뷰 타입
    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    // 아이템
    private var productItemList: ArrayList<ProductItem> = ArrayList()

    // 이벤트 리스너
    override var onClickFuncOfProduct: ((ProductItem) -> Unit)? = null
    override var onClickFuncOfBasketBtn: ((Int, Int) -> Unit)? = null

    // 메서드
    override fun getItemCount() = this.productItemList.size

    override fun clearItem() = this.productItemList.clear()

    override fun getItem(position: Int) = this.productItemList[position]

    override fun getItems() = productItemList

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyAdapterByRange(start:Int, count:Int) = notifyItemRangeInserted(start, count)

    override fun notifyLastItemRemoved() {
        notifyItemRemoved(productItemList.lastIndex)
    }

    override fun addItems(productItems: ArrayList<ProductItem>) {
        productItemList.addAll(productItems)
        productItemList.add(ProductItem(
            productId = -1,
            productName = " ",
            productPrice = 0,
            productImageUrl = ""
        ))
    }

    override fun deleteLoading() {
        productItemList.removeAt(productItemList.lastIndex)
    }

    // 뷰 타입 지정
    override fun getItemViewType(position: Int): Int {
        return when(productItemList[position].productId){
            -1 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProductViewHolder){
            productItemList[position].let {
                holder.onBind(productItem = productItemList[position], usingBy = usingBy)
            }
        } else {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_ITEM -> {
                val itemBinding = ProductItemBinding.inflate(LayoutInflater.from(parent.context))
                ProductViewHolder(
                    parent = parent,
                    itemBinding = itemBinding,
                    listenerFuncOfProduct = onClickFuncOfProduct,
                    listenerFuncOfBasketBtn = onClickFuncOfBasketBtn
                )
            }

            else -> {
                val itemBinding = RecyclerProductLoadingItemBinding.inflate(LayoutInflater.from(parent.context))
                ProductLoadingViewHolder(
                    parent = parent,
                    itemBinding = itemBinding
                )
            }
        }
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
            if (usingBy == "home") {
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
            } else if (usingBy == "category" || usingBy == "search") {
                val insideSpaceByPx = (4 * density).toInt()

                if (itemPosition % 2 != 0) {
                    outRect.left = insideSpaceByPx
                } else {
                    outRect.right = insideSpaceByPx
                }

                outRect.bottom = (28 * density).toInt()
            }
        }
    }
}