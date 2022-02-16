package kr.co.younhwan.happybuyer.view.main.home.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.databinding.CategoryItemBinding
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.HomeAdapterContract

class HomeAdapter :
    RecyclerView.Adapter<HomeViewHolder>(), HomeAdapterContract.Model, HomeAdapterContract.View {

    private lateinit var categoryList: ArrayList<CategoryItem>

    override var onClickFunc: ((Int) -> Unit)? = null

    override fun getItemCount() = categoryList.size

    override fun getItem(position: Int) = categoryList[position]

    override fun addItems(categoryItems: ArrayList<CategoryItem>) {
        this.categoryList = categoryItems
    }

    override fun clearItem() {
        categoryList.clear()
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val rowBinding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        return HomeViewHolder(parent, rowBinding, onClickFunc)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        categoryList[position].let {
            holder.onBind(it, position)
        }
    }

    inner class RecyclerDecoration(private val divHeight : Int) :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom= divHeight
        }
    }
}