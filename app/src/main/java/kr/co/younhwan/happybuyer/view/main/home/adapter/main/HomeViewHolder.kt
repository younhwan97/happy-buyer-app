package kr.co.younhwan.happybuyer.view.main.home.adapter.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.databinding.CategoryItemBinding

class HomeViewHolder(
    parent: ViewGroup,
    categoryItemBinding: CategoryItemBinding,
    private val listenerFunc: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(categoryItemBinding.root) {

    private val imageView by lazy {
        categoryItemBinding.rowImageView
    }

    private val textView by lazy {
        categoryItemBinding.rowTextView
    }

    fun onBind(item: CategoryItem, position: Int) {
        textView.text = item.title
        imageView.setImageResource(item.resource)

        imageView.setOnClickListener {
            listenerFunc?.invoke(position)
        }

        textView.setOnClickListener {
            listenerFunc?.invoke(position)
        }
    }
}