package kr.co.younhwan.happybuyer.view.main.home.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.databinding.RecyclerCategoryItemBinding
import kr.co.younhwan.happybuyer.view.main.home.adapter.main.contract.HomeAdapterContract

class HomeAdapter :
    RecyclerView.Adapter<HomeViewHolder>(), HomeAdapterContract.Model, HomeAdapterContract.View {

    private lateinit var categoryList: ArrayList<CategoryItem>

    override var onClickFuncOfCategoryItem: ((Int) -> Unit)? = null

    override fun getItemCount() = categoryList.size

    override fun getItem(position: Int) = categoryList[position]

    override fun clearItem() = categoryList.clear()

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun addItems(categoryItems: ArrayList<CategoryItem>) {
        this.categoryList = categoryItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val categoryItemBinding = RecyclerCategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        return HomeViewHolder(
            parent,
            categoryItemBinding,
            onClickFuncOfCategoryItem
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        categoryList[position].let {
            holder.onBind(it, position)
        }
    }
}