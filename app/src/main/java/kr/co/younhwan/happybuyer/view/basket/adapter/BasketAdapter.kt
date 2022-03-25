package kr.co.younhwan.happybuyer.view.basket.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.databinding.RecyclerBasketItemBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.contract.BasketAdapterContract

class BasketAdapter :
    RecyclerView.Adapter<BasketViewHolder>(),
    BasketAdapterContract.Model,
    BasketAdapterContract.View {
    
    // 아이템 (리사이클러뷰)
    private lateinit var basketItemList: ArrayList<BasketItem>

    // 이벤트 리스너
    override var onClickFunOfCheckBox: ((Int, Boolean) -> Unit)? = null

    override var onClickFunOfPlusBtn: ((BasketItem, Int) -> Unit)? = null

    override var onClickFunOfMinusBtn: ((BasketItem, Int) -> Unit)? = null

    override var onClickFunOfDeleteBtn: ((BasketItem, Int) -> Unit)? = null
    
    // 메서드
    override fun getItemCount() = basketItemList.size

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        basketItemList[position].let {
             holder.onBind(it)
        }
    }

    override fun onBindViewHolder(
        holder: BasketViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (payloads[0]) {
                "plus" -> {
                    holder.onBindCount(basketItemList[position])
                }

                "minus" -> {
                    holder.onBindCount(basketItemList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val itemBinding = RecyclerBasketItemBinding.inflate(LayoutInflater.from(parent.context))
        return BasketViewHolder(
            parent = parent,
            basketItemBinding = itemBinding,
            listenerFunOfCheckBox = onClickFunOfCheckBox,
            listenerFunOfPlusBtn = onClickFunOfPlusBtn,
            listenerFunOfMinusBtn = onClickFunOfMinusBtn,
            listenerFunOfDeleteBtn = onClickFunOfDeleteBtn
        )
    }

    override fun addItems(productItems: ArrayList<BasketItem>) {
        basketItemList = productItems
    }

    override fun clearItem() = basketItemList.clear()

    override fun getItem(position: Int) = basketItemList[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyItem(position: Int) = notifyItemChanged(position)

    override fun notifyItemByUsingPayload(position: Int, payload: String) = notifyItemChanged(position, payload)

    override fun updateItem(position: Int, basketItem: BasketItem) {
        basketItemList[position] = basketItem
    }

    override fun updateItemChecked(position: Int, newStatus: Boolean) {
        basketItemList[position].isChecked = newStatus
    }

    override fun updateItemCount(position: Int, count: Int) {
        basketItemList[position].countInBasket = count
    }

    override fun deleteItem(position: Int) {
        basketItemList.removeAt(position)
        notifyItemRemoved(position)
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

            outRect.top = (16 * density).toInt()

            if(itemPosition == basketItemList.size - 1){
                outRect.bottom = (16 * density).toInt()
            }
        }
    }
}