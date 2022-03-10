package kr.co.younhwan.happybuyer.view.search.adapter.suggested

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.databinding.RecyclerSuggestedSearchItemBinding

class SuggestedViewHolder(
    parent: ViewGroup,
    recyclerSuggestedSearchItemBinding: RecyclerSuggestedSearchItemBinding,
    private val listenerFuncOfSuggestedSearch: ((String) -> Unit)?
    ) :RecyclerView.ViewHolder(recyclerSuggestedSearchItemBinding.root){

    private val suggestedText by lazy {
        recyclerSuggestedSearchItemBinding.suggestedSearchText
    }

    fun onBind(item: String){
        suggestedText.text = item
        suggestedText.setOnClickListener {
            listenerFuncOfSuggestedSearch?.invoke(item)
        }
    }
}