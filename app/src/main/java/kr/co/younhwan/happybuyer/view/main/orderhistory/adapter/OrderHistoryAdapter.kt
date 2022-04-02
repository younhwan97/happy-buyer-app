package kr.co.younhwan.happybuyer.view.main.orderhistory.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderHistoryItemBinding
import kr.co.younhwan.happybuyer.view.orderhistory.adapter.contract.OrderHistoryAdapterContract

class OrderHistoryAdapter :
    RecyclerView.Adapter<OrderHistoryViewHolder>(),
    OrderHistoryAdapterContract.Model,
    OrderHistoryAdapterContract.View {

    // 아이템
    private lateinit var orderHistoryItemList: ArrayList<OrderItem>

    // 이벤트 리스너
    override var onClickFun: ((OrderItem) -> Unit)? = null

    // 메서드
    override fun getItemCount() = orderHistoryItemList.size

    override fun addItems(orderHistoryItems: ArrayList<OrderItem>) {
        orderHistoryItemList = orderHistoryItems
    }

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val itemBinding =
            RecyclerOrderHistoryItemBinding.inflate(LayoutInflater.from(parent.context))
        return OrderHistoryViewHolder(
            parent = parent,
            orderHistoryItemBinding = itemBinding,
            listenerFun = onClickFun
        )
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        orderHistoryItemList[position].let {
            holder.onBind(it)
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

            if(itemPosition == 0){
                outRect.top = (16 * density).toInt()
            }
            outRect.bottom = (12 * density).toInt()
        }
    }
}
