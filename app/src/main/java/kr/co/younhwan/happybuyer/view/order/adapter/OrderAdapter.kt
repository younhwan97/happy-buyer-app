package kr.co.younhwan.happybuyer.view.order.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderProductItemBinding
import kr.co.younhwan.happybuyer.view.order.adapter.contract.OrderAdapterContract

class OrderAdapter :
    RecyclerView.Adapter<OrderViewHolder>(),
    OrderAdapterContract.View,
    OrderAdapterContract.Model {

    // 아이템
    private lateinit var orderItemList: ArrayList<BasketItem>

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