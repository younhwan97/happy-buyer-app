package kr.co.younhwan.happybuyer.view.main.home.adapter.event

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.home.adapter.event.contract.EventAdapterContract
import kr.co.younhwan.happybuyer.databinding.EventItemBinding

class EventAdapter : RecyclerView.Adapter<EventViewHolder>(), EventAdapterContract.Model, EventAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemBinding = EventItemBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(
            parent,
            itemBinding
        )
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
}