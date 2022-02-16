package kr.co.younhwan.happybuyer.view.category.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryAdapter :
    RecyclerView.Adapter<CategoryViewHolder>(), CategoryAdapterContract.Model,
    CategoryAdapterContract.View {

    private lateinit var categoryItemList: ArrayList<CategoryItem>

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        categoryItemList[position].let {
            holder.onBind(it, position)
        }
    }

    override fun getItemCount() = categoryItemList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context))
        val holder = CategoryViewHolder(parent, itemBinding)
        // rowBinding.root.setOnClickListener(holder)
        return holder
    }

    override fun addItems(categoryItems: ArrayList<CategoryItem>) {
        this.categoryItemList = categoryItems
    }

    override fun clearItem() {
        categoryItemList.clear()
    }

    override fun getItem(position: Int) = categoryItemList[position]

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }
}


//inner class RecyclerAdapter : RecyclerView.Adapter<CategoryFragment.RecyclerAdapter.ViewHolderClass>(){
//    // 항목 구성을 위한 사용할 ViewHolder 객체가 필요할 때 사용되는 메서드
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
//        val itemBinding = ItemBinding.inflate(layoutInflater)
//        return ViewHolderClass(itemBinding)
//    }
//
//    override fun getItemCount(): Int {
//        return productIdList.size
//    }
//
//    // ViewHolder 를 이용해 항목 내의 객체에 데이터를 셋팅
//    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
//
//        val metrics : DisplayMetrics = resources.displayMetrics
//        val outSidePadding = Math.round(15 * metrics.density)
//        val inSidePadding = Math.round(5 * metrics.density)
//        val topPadding = Math.round(5 * metrics.density)
//
//        if(position % 2 == 0)
//            holder.itemContainer.setPadding(outSidePadding, topPadding, inSidePadding,0)
//        else
//            holder.itemContainer.setPadding(inSidePadding, topPadding, outSidePadding,0)
//
//        val url = productImageUriList[position]
//
//        holder.apply {
//            Glide.with(holder.itemView.context).load(url).into(holder.itemImage)
//            itemName.text = productNameList[position]
//            itemPrice.text = DecimalFormat("#,###").format(productPriceList[position])
//        }
//
//        // Set click event listener
//        holder.shoppingCartBtn.setOnClickListener {
//
//        }
//
//        holder.hearBtn.setOnClickListener {
//
//        }
//
//        holder.itemName.setOnClickListener {
//
//        }
//
//        holder.itemImage.setOnClickListener {
//
//        }
//
//        holder.itemPrice.setOnClickListener {
//
//        }
//    }
//}