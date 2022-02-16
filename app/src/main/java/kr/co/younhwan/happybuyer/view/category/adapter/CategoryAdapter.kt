package kr.co.younhwan.happybuyer.view.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryAdapter :
    RecyclerView.Adapter<CategoryViewHolder>(), CategoryAdapterContract.Model,
    CategoryAdapterContract.View {

    private lateinit var productItemList: ArrayList<ProductItem>

    override var onClickFuncHeartBtn: ((Int) -> Unit)? = null

    override var onClickFuncShoppingCartBtn: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        productItemList[position].let {
            holder.onBind(it, position)
        }
    }

    override fun getItemCount() = productItemList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context))
        val holder = CategoryViewHolder(parent, itemBinding, onClickFuncHeartBtn, onClickFuncShoppingCartBtn)
        // rowBinding.root.setOnClickListener(holder)
        return holder
    }

    override fun addItems(productItems: ArrayList<ProductItem>) {
        this.productItemList = productItems
    }

    override fun clearItem() {
        productItemList.clear()
    }

    override fun getItem(position: Int) = productItemList[position]

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }
}