package kr.co.younhwan.happybuyer.view.search.adapter.recent

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.SearchItem
import kr.co.younhwan.happybuyer.databinding.RecyclerRecentSearchItemBinding

class RecentViewHolder (
    parent: ViewGroup,
    recentItemBinding: RecyclerRecentSearchItemBinding,
    private val listenerFuncOfDeleteBtn: ((String, Int) -> Unit)?,
    private val listenerFuncOfRecentSearch: ((String) -> Unit)?
) : RecyclerView.ViewHolder(recentItemBinding.root) {

    private val keyword by lazy {
        recentItemBinding.recentSearchKeyword
    }

    private val deleteBtn by lazy {
        recentItemBinding.recentSearchDeleteBtn
    }

    fun onBind(item: SearchItem) {
        keyword.text = item.keyword
        keyword.setOnClickListener {
            listenerFuncOfRecentSearch?.invoke(item.keyword)
        }

        deleteBtn.isClickable = true
        deleteBtn.setOnClickListener {
            deleteBtn.isClickable = false
            listenerFuncOfDeleteBtn?.invoke(item.keyword, adapterPosition)
        }
    }
}