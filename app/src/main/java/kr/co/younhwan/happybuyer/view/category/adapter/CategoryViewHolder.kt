package kr.co.younhwan.happybuyer.view.category.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.databinding.ItemBinding

class CategoryViewHolder(
    parent: ViewGroup,
    itemBinding: ItemBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val itemName by lazy {
        itemBinding.itemName
    }

    private val itemPrice by lazy {
        itemBinding.itemPrice
    }

    private val itemImage by lazy {
        itemBinding.itemImage
    }


    fun onBind(categoryItem: CategoryItem, position: Int){
        itemName.text = categoryItem.title
        itemPrice.text = categoryItem.price.toString()
        // itemImage.setImageResource()
    }
}