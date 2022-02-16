package kr.co.younhwan.happybuyer.view.main.home.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.databinding.RowBinding
import kr.co.younhwan.happybuyer.view.main.home.adapter.contract.HomeAdapterContract

class HomeAdapter :
    RecyclerView.Adapter<HomeViewHolder>(), HomeAdapterContract.Model, HomeAdapterContract.View {

    private lateinit var imageList: ArrayList<ImageItem>

    override var onClickFunc: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        imageList[position].let {
            holder.onBind(it, position)
        }
    }

    override fun getItemCount() = imageList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context))
        val holder = HomeViewHolder(parent, rowBinding, onClickFunc)
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