package kr.co.younhwan.happybuyer.view.basket.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerBasketItemBinding
import kr.co.younhwan.happybuyer.view.basket.adapter.contract.BasketAdapterContract

class BasketAdapter :
    RecyclerView.Adapter<BasketViewHolder>(),
    BasketAdapterContract.Model,
    BasketAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncOfPlusBtn: ((Int, Int) -> Unit)? = null

    override var onClickFuncOfMinusBtn: ((Int, Int) -> Unit)? = null

    override var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)? = null

    override fun getItemCount() = productItemList.size

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        productItemList[position].let {
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
                    productItemList[position].countInBasket += 1
                    holder.onBindBasketCount(productItemList[position])
                }

                "minus" -> {
                    productItemList[position].countInBasket -= 1
                    holder.onBindBasketCount(productItemList[position])
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

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun clearItem() = this.productItemList.clear()

    override fun getItem(position: Int) = this.productItemList[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyItem(position: Int) = notifyItemChanged(position)

    inner class RecyclerDecoration :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.bottom = 20
        }
    }

    override fun notifyItemByUsingPayload(position: Int, payload: String) {
        notifyItemChanged(position, payload)
    }

    override fun deleteItem(position: Int) {
        productItemList.removeAt(position)
        notifyItemRemoved(position)
    }
}