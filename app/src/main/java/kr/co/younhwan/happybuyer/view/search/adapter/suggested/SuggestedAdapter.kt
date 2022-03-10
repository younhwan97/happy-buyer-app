package kr.co.younhwan.happybuyer.view.search.adapter.suggested

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.databinding.RecyclerSuggestedSearchItemBinding
import kr.co.younhwan.happybuyer.view.search.adapter.suggested.contract.SuggestedAdapterContract

class SuggestedAdapter : RecyclerView.Adapter<SuggestedViewHolder>(), Filterable,
    SuggestedAdapterContract.Model, SuggestedAdapterContract.View {

    // 리사이클러 뷰를 생성하는데 사용할 '필터링 된' 데이터
    private lateinit var suggestedItemList: ArrayList<String>

    // 필터링 하기전 전체 데이터
    // -> 처음에 초기화 된 이후 변하면 안됨
    val searchHistoryItemList = ArrayList<String>()

    override var onClickFuncOfSuggestedSearch: ((String) -> Unit)? = null

    override fun getItem(position: Int): String = suggestedItemList[position]

    override fun getItemCount() = suggestedItemList.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun clearItem() = suggestedItemList.clear()

    override fun addItems(suggestedItems: ArrayList<String>) {
        suggestedItemList = suggestedItems
    }

    override fun addItemsOnHistoryItemList(searchHistoryItems: ArrayList<String>) {
        searchHistoryItemList.addAll(searchHistoryItems)
    }

    override fun onBindViewHolder(holder: SuggestedViewHolder, position: Int) {
        suggestedItemList[position].let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedViewHolder {
        val recyclerSuggestedSearchItemBinding =
            RecyclerSuggestedSearchItemBinding.inflate(LayoutInflater.from(parent.context))
        return SuggestedViewHolder(
            parent = parent,
            recyclerSuggestedSearchItemBinding = recyclerSuggestedSearchItemBinding,
            listenerFuncOfSuggestedSearch = onClickFuncOfSuggestedSearch
        )
    }

    /* Filter */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filteredList = ArrayList<String>() // '필터링 된' 아이템

                if(searchHistoryItemList.size != 0){
                    if (p0.toString().isEmpty()) {
                        filteredList.addAll(searchHistoryItemList)
                    } else {
                        for (item in searchHistoryItemList) {
                            if (item.contains(p0.toString())) {
                                filteredList.add(item)
                            }
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                clearItem()
                addItems(p1?.values as ArrayList<String>)
                notifyDataSetChanged()
            }
        }
    }
}