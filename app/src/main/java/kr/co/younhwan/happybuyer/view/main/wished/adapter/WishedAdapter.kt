package kr.co.younhwan.happybuyer.view.main.wished.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerWishedItemBinding
import kr.co.younhwan.happybuyer.view.main.wished.adapter.contract.WishedAdapterContract

class WishedAdapter :
    RecyclerView.Adapter<WishedViewHolder>(),
    WishedAdapterContract.Model,
    WishedAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)? = null

    override var onClickFuncOfBasketBtn: ((Int) -> Unit)? = null

    override fun getItemCount() = productItemList.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun clearItem() = productItemList.clear()

    override fun getItem(position: Int) = productItemList[position]

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun deleteItem(position: Int) {
        productItemList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: WishedViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishedViewHolder {
        val itemBinding = RecyclerWishedItemBinding.inflate(LayoutInflater.from(parent.context))
        return WishedViewHolder(
            parent,
            itemBinding,
            onClickFuncOfDeleteBtn,
            onClickFuncOfBasketBtn
        )
    }

    inner class RecyclerDecoration :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val density = parent.resources.displayMetrics.density
            val spaceByPx = (16 * density).toInt()

            outRect.right = spaceByPx
            outRect.left= spaceByPx
        }
    }
}