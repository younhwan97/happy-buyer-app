package kr.co.younhwan.happybuyer.view.main.home.adapter.popular

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.PopularItemBinding
import kr.co.younhwan.happybuyer.view.main.home.adapter.popular.contract.PopularAdapterContract

class PopularAdapter :
    RecyclerView.Adapter<PopularViewHolder>(), PopularAdapterContract.Model, PopularAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val itemBinding = PopularItemBinding.inflate(LayoutInflater.from(parent.context))
        return PopularViewHolder(
            parent,
            itemBinding
        )
    }

    override fun getItemCount() = productItemList.size

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun clearItem()  = this.productItemList.clear()

    override fun getItem(position: Int) = this.productItemList[position]

    override fun notifyAdapter()  = notifyDataSetChanged()

    inner class RecyclerDecoration :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemPosition = parent.getChildAdapterPosition(view)

            if(itemPosition % 2 == 0){
                outRect.right = 24
            } else {
                outRect.left = 24
            }

            outRect.bottom = 10
        }
    }
}