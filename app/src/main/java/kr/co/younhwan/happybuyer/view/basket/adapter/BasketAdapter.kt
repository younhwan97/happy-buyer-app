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
    override var onClickFuncOfPlusBtn: ((Int, Int) -> Unit)? = null // 플러스 버튼
    override var onClickFuncOfMinusBtn: ((Int, Int) -> Unit)? = null // 마이너스 버튼
    override var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)? = null // 삭제 버튼





    
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
                    basketItemList[position].countInBasket += 1
                   // holder.onBindBasketCount(productItemList[position])
                }

                "minus" -> {
                    basketItemList[position].countInBasket -= 1
                  //  holder.onBindBasketCount(productItemList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val itemBinding = RecyclerBasketItemBinding.inflate(LayoutInflater.from(parent.context))
        return BasketViewHolder(
            parent,
            itemBinding,
            onClickFuncOfPlusBtn,
            onClickFuncOfMinusBtn,
            onClickFuncOfDeleteBtn
        )
    }

    override fun addItems(productItems: ArrayList<BasketItem>) {
        this.basketItemList = productItems
    }

    override fun clearItem() = this.basketItemList.clear()

    override fun getItem(position: Int) = this.basketItemList[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyItem(position: Int) = notifyItemChanged(position)

    override fun notifyItemByUsingPayload(position: Int, payload: String) {
        notifyItemChanged(position, payload)
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


    override fun updateItem(position: Int, basketItem: BasketItem) {
        basketItemList[position] = basketItem
    }
}