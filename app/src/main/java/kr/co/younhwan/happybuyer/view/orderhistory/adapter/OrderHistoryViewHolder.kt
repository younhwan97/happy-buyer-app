package kr.co.younhwan.happybuyer.view.orderhistory.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.databinding.RecyclerOrderHistoryItemBinding

class OrderHistoryViewHolder(
    private val parent: ViewGroup,
    orderHistoryItemBinding: RecyclerOrderHistoryItemBinding,
    private val listenerFun: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(orderHistoryItemBinding.root) {

    private val itemId by lazy {
        orderHistoryItemBinding.orderHistoryItemId
    }

    private val itemName by lazy {
        orderHistoryItemBinding.orderHistoryItemName
    }

    private val itemDate by lazy {
        orderHistoryItemBinding.orderHistoryItemDate
    }

    private val itemPayment by lazy {
        orderHistoryItemBinding.orderHistoryItemPayment
    }

    private val itemPrice by lazy {
        orderHistoryItemBinding.orderHistoryItemPrice
    }

    private val itemStatus by lazy {
        orderHistoryItemBinding.orderHistoryItemDeliveryStatus
    }

    fun onBind(orderHistoryItem: OrderItem) {
        itemId.text = orderHistoryItem.orderId.toString()

        itemName.text = orderHistoryItem.name

        itemDate.text = orderHistoryItem.date

        itemPayment.text = orderHistoryItem.payment

        itemPrice.text = orderHistoryItem.bePaidPrice.plus("원")

        itemStatus.text = orderHistoryItem.status

        itemView.setOnClickListener {
            listenerFun?.invoke(orderHistoryItem.orderId)
        }
    }
}