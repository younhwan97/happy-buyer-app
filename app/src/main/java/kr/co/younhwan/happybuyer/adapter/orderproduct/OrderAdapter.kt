package kr.co.younhwan.happybuyer.adapter.orderproduct

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderProductItemBinding
import kr.co.younhwan.happybuyer.adapter.orderproduct.contract.OrderAdapterContract

class OrderAdapter :
    RecyclerView.Adapter<OrderViewHolder>(),
    OrderAdapterContract.View,
    OrderAdapterContract.Model {

    // 아이템
    private var orderItemList: ArrayList<BasketItem> = ArrayList()

    // 메서드
    override fun getItemCount() = orderItemList.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        orderItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemBinding = RecyclerOrderProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return OrderViewHolder(
            parent = parent,
            orderItemBinding = itemBinding
        )
    }

    override fun addItems(orderItems: ArrayList<BasketItem>) {
        orderItemList = orderItems
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun getItem(position: Int) = orderItemList[position]

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

            outRect.bottom = (16 * density).toInt()
        }
    }
}