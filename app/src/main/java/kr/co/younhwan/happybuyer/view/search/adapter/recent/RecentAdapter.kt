package kr.co.younhwan.happybuyer.view.search.adapter.recent

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.SearchItem
import kr.co.younhwan.happybuyer.databinding.RecyclerRecentSearchItemBinding
import kr.co.younhwan.happybuyer.view.search.adapter.recent.contract.RecentAdapterContract

class RecentAdapter : RecyclerView.Adapter<RecentViewHolder>(), RecentAdapterContract.Model, RecentAdapterContract.View {

    private lateinit var recentSearchItem: ArrayList<SearchItem>

    override var onClickFuncOfDeleteBtn: ((String, Int) -> Unit)? = null

    override var onClickFuncOfRecentSearch: ((String) -> Unit)? = null

    override fun getItemCount() = recentSearchItem.size

    override fun clearItem() = recentSearchItem.clear()

    override fun getItem(position: Int) = recentSearchItem[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyRemoved(position: Int) = notifyItemRemoved(position)

    override fun addItems(recentSearchItems: ArrayList<SearchItem>) {
        this.recentSearchItem = recentSearchItems
    }

    override fun deleteItem(position: Int) {
        recentSearchItem.removeAt(position)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        recentSearchItem[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val recentSearchItemBinding = RecyclerRecentSearchItemBinding.inflate(LayoutInflater.from(parent.context))
        return RecentViewHolder(
            parent,
            recentSearchItemBinding,
            onClickFuncOfDeleteBtn,
            onClickFuncOfRecentSearch
        )
    }

    inner class RecyclerDecoration :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemPosition = parent.getChildAdapterPosition(view)

            val density = parent.resources.displayMetrics.density
            val spaceByPx = (12 * density).toInt()

            if(itemPosition % 2 == 0){
                outRect.right = spaceByPx
            } else {
                outRect.left = spaceByPx
            }
        }
    }
}