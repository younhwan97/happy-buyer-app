package kr.co.younhwan.happybuyer.view.main.home.adapter.event

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.home.adapter.event.contract.EventAdapterContract
import kr.co.younhwan.happybuyer.databinding.RecyclerEventItemBinding

class EventAdapter : RecyclerView.Adapter<EventViewHolder>(), EventAdapterContract.Model, EventAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncOfWishedBtn: ((Int, Int) -> Unit)? = null

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemBinding = RecyclerEventItemBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(
            parent,
            itemBinding,
            onClickFuncOfWishedBtn,

        )
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (payloads[0]) {
                "wished" -> {
                    productItemList[position].isWished = !productItemList[position].isWished
                    holder.onBindWishedState(productItemList[position])
                }

                "basket" -> {
                    // holder.onBindBasketState(productItemList[position], position.toString())
                }
            }
        }
    }

    override fun getItemCount() = this.productItemList.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun clearItem() = this.productItemList.clear()

    override fun getItem(position: Int): ProductItem = this.productItemList[position]

    inner class RecyclerDecoration :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemPosition = parent.getChildAdapterPosition(view)

            val spaceByDp = 24
            val density = parent.resources.displayMetrics.density
            val spaceByPx = (spaceByDp * density).toInt()

            /* first position */
            if(itemPosition == 0 )
                outRect.left = spaceByPx

            outRect.right = spaceByPx
            outRect.bottom = spaceByPx
        }
    }

    override fun updateProduct(position: Int, productItem: ProductItem) {
        this.productItemList[position] = productItem
    }

    override fun notifyItem(position: Int)  = notifyItemChanged(position)

    override fun notifyItemByUsingPayload(position: Int, payload: String) = notifyItemChanged(position, payload)
}