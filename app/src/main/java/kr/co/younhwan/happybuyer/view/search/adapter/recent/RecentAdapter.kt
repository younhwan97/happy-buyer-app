package kr.co.younhwan.happybuyer.view.search.adapter.recent

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.RecentItem
import kr.co.younhwan.happybuyer.databinding.RecyclerRecentSearchItemBinding
import kr.co.younhwan.happybuyer.view.search.adapter.recent.contract.RecentAdapterContract

class RecentAdapter : RecyclerView.Adapter<RecentViewHolder>(), RecentAdapterContract.Model, RecentAdapterContract.View {

    // 아이템
    private var recentItemList: ArrayList<RecentItem> = ArrayList()

    // 이벤트 리스너
    override var onClickFuncOfDeleteBtn: ((String, Int) -> Unit)? = null

    override var onClickFuncOfRecentSearch: ((String) -> Unit)? = null

    // 메서드
    override fun getItemCount() = recentItemList.size

    override fun clearItem() = recentItemList.clear()

    override fun getItem(position: Int) = recentItemList[position]

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun notifyRemoved(position: Int) = notifyItemRemoved(position)

    override fun addItems(recentItems: ArrayList<RecentItem>) {
        recentItemList.addAll(recentItems)
    }

    override fun deleteItem(position: Int) {
        recentItemList.removeAt(position)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        recentItemList[position].let {
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

    // 데코레이션
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