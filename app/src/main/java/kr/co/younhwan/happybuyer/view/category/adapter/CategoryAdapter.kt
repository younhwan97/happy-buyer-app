package kr.co.younhwan.happybuyer.view.category.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.RecyclerProductItemBinding
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryAdapter :
    RecyclerView.Adapter<CategoryViewHolder>(),
    CategoryAdapterContract.Model,
    CategoryAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncOfWishedBtn: ((Int, Int) -> Unit)? = null

    override var onClickFuncOfBasketBtn: ((Int, Int) -> Unit)? = null

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (payloads[0]) {
                "wished" -> {
                    productItemList[position].isWished = !productItemList[position].isWished
                    holder.onBindWishedState(productItemList[position])
                }

                "basket" -> {
                   // holder.onBindBasketState(productItemList[position], position.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = RecyclerProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(
            parent,
            itemBinding,
            onClickFuncOfWishedBtn,
            onClickFuncOfBasketBtn
        )
    }

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun getItemCount() = productItemList.size

    override fun clearItem() = productItemList.clear()

    override fun getItem(position: Int) = productItemList[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyItem(position: Int) = notifyItemChanged(position)

    override fun notifyItemByUsingPayload(position: Int, payload: String) {
        notifyItemChanged(position, payload)
    }
}