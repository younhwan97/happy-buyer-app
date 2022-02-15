package kr.co.younhwan.happybuyer.view.main.home.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.databinding.RowBinding
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.CategoryAdapterContract

class CategoryAdapter :
    RecyclerView.Adapter<CategoryViewHolder>(), CategoryAdapterContract.Model, CategoryAdapterContract.View {

    private lateinit var imageList: ArrayList<ImageItem>

    override var onClickFunc: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        imageList[position].let {
            holder.onBind(it, position)
        }
    }

    override fun getItemCount() = imageList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context))
        val holder = CategoryViewHolder(parent, rowBinding, onClickFunc)
        // rowBinding.root.setOnClickListener(holder)
        return holder
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun addItems(imageItems: ArrayList<ImageItem>) {
        this.imageList = imageItems
    }

    override fun clearItem() {
        imageList.clear()
    }

    override fun getItem(position: Int) = imageList[position]

    inner class RecyclerDecoration(private val divHeight : Int) :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom= divHeight
        }
    }
}

//override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragment.RecyclerAdapter.ViewHolderClass {
//    // View를 생성한다.
//    val rowBinding = RowBinding.inflate(layoutInflater)
//    val holder = ViewHolderClass(rowBinding)
//    rowBinding.root.setOnClickListener(holder)
//
//    return holder
//}
//
//override fun onBindViewHolder(holder: HomeFragment.RecyclerAdapter.ViewHolderClass, position: Int) {
//    // View를 bind 한다.
//    holder.rowImageView.setImageResource(imgRes[position])
//    holder.rowTextView.text = label[position]
//}
//
//override fun getItemCount(): Int {
//    //  현재 View에 노출할 아이템의 카운트 수
//    return label.size
//}
//
//fun notifyAdapter(){
//    notifyDataSetChanged()
//}

//class CategoryAdapter :
//    RecyclerView.Adapter<CategoryViewHolder>(), CategoryAdapterContract.Model, CategoryAdapterContract.View {
//
//    private lateinit var imageList: ArrayList<ImageItem>
//
//    // View
//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        imageList[position].let {
//            holder.onBind(it, position)
//        }
//    }
//
//    override fun getItemCount() = imageList.size
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(parent)
//
//    override fun notifyAdapter() {
//        notifyDataSetChanged()
//    }
//
//    override fun addItems(imageItems: ArrayList<ImageItem>) {
//        this.imageList = imageItems
//    }
//
//    override fun clearItem() {
//        imageList.clear()
//    }
//
//    override fun getItem(position: Int) = imageList[position]
//
//    inner class RecyclerDecoration(private val divHeight : Int) :RecyclerView.ItemDecoration() {
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//            super.getItemOffsets(outRect, view, parent, state)
//            outRect.bottom= divHeight
//        }
//    }
//
//}