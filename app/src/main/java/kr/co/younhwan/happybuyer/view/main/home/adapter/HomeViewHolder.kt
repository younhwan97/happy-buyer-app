package kr.co.younhwan.happybuyer.view.main.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.databinding.RowBinding

class HomeViewHolder(
    parent: ViewGroup,
    rowBinding: RowBinding,
    private val listenerFunc: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(rowBinding.root) {

    private val imageView by lazy {
        rowBinding.rowImageView
    }

    private val textView by lazy {
        rowBinding.rowTextView
    }

    fun onBind(item: ImageItem, position: Int) {
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