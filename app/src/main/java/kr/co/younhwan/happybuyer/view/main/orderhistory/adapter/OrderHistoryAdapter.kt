package kr.co.younhwan.happybuyer.view.main.orderhistory.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderHistoryItemBinding
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderHistoryLoadingItemBinding
import kr.co.younhwan.happybuyer.databinding.RecyclerProductLoadingItemBinding
import kr.co.younhwan.happybuyer.view.orderhistory.adapter.contract.OrderHistoryAdapterContract

class OrderHistoryAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    OrderHistoryAdapterContract.Model,
    OrderHistoryAdapterContract.View {

    // 뷰 타입
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    // 아이템
    private var orderHistoryItemList: ArrayList<OrderItem> = ArrayList()

    // 이벤트 리스너
    override var onClickFun: ((OrderItem) -> Unit)? = null

    // 메서드
    override fun getItemCount() = orderHistoryItemList.size

    override fun clearItem() {
        orderHistoryItemList.clear()
    }

    override fun notifyLastItemRemoved() {
        notifyItemRemoved(orderHistoryItemList.lastIndex)
    }

    override fun notifyAdapterByRange(start: Int, count: Int) = notifyItemRangeInserted(start, count)

    override fun addItems(orderHistoryItems: ArrayList<OrderItem>) {
        orderHistoryItemList.addAll(orderHistoryItems)
        orderHistoryItemList.add(
            OrderItem(
                orderId = -1,
                name = "",
                status = "",
                date = "",
                receiver = "",
                phone = "",
                address = "",
                requirement = "",
                point = "",
                detectiveHandlingMethod = "",
                payment = "",
                originalPrice = "",
                eventPrice = "",
                bePaidPrice = "",
                products = ArrayList()
            )
        )
    }

    override fun deleteLoading() {
        orderHistoryItemList.removeAt(orderHistoryItemList.lastIndex)
    }

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun getItemViewType(position: Int): Int {
        return when (orderHistoryItemList[position].orderId) {
            -1 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val itemBinding =
                    RecyclerOrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context))
                OrderHistoryViewHolder(
                    parent = parent,
                    orderHistoryItemBinding = itemBinding,
                    listenerFun = onClickFun
                )
            }

            else -> {
                val itemBinding =
                    RecyclerOrderHistoryLoadingItemBinding.inflate(LayoutInflater.from(parent.context))
                OrderHistoryLoadingViewHolder(
                    parent = parent,
                    orderHistoryLoadingItemBinding = itemBinding
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderHistoryViewHolder) {
            orderHistoryItemList[position].let {
                holder.onBind(it)
            }
        } else {

        }
    }

    // 데코레이션
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

            if (itemPosition == 0) {
                outRect.top = (16 * density).toInt()
            }
            outRect.bottom = (8 * density).toInt()
        }
    }
}
