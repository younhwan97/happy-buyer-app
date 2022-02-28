package kr.co.younhwan.happybuyer.view.main.wished.adapter

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerWishedItemBinding
import kr.co.younhwan.happybuyer.view.main.wished.adapter.contract.WishedAdapterContract

class WishedAdapter :
    RecyclerView.Adapter<WishedViewHolder>(),
    WishedAdapterContract.Model,
    WishedAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncOfDeleteBtn: ((Int, Int) -> Unit)? = null

    override var onClickFuncOfBasketBtn: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: WishedViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishedViewHolder {
        val itemBinding = RecyclerWishedItemBinding.inflate(LayoutInflater.from(parent.context))
        return WishedViewHolder(
            parent,
            itemBinding,
            onClickFuncOfDeleteBtn,
            onClickFuncOfBasketBtn
        )
    }

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun getItemCount() = productItemList.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun clearItem() = productItemList.clear()

    override fun getItem(position: Int) = productItemList[position]

    override fun deleteItem(position: Int) {
        productItemList.removeAt(position)
        notifyItemRemoved(position)
    }
}