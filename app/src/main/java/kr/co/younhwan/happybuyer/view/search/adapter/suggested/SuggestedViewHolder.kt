package kr.co.younhwan.happybuyer.view.search.adapter.suggested

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.databinding.RecyclerSuggestedItemBinding

class SuggestedViewHolder(
    parent: ViewGroup,
    recyclerSuggestedItemBinding: RecyclerSuggestedItemBinding
    ) :RecyclerView.ViewHolder(recyclerSuggestedItemBinding.root){

    private val productName by lazy {
        recyclerSuggestedItemBinding.textView5
    }

    fun onBind(item: String){
        this.productName.text = item
    }
}