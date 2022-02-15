package kr.co.younhwan.happybuyer.view.main.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.databinding.RowBinding

class CategoryViewHolder(
    parent: ViewGroup,
    rowBinding: RowBinding,
    private val listenerFunc: ((Int) -> Unit)?
) : RecyclerView.ViewHolder(rowBinding.root){

    private val imageView by lazy {
        rowBinding.rowImageView
    }

    private val textView by lazy {
        rowBinding.rowTextView
    }

    fun onBind(item: ImageItem, position: Int) {
        textView.text = item.title
        imageView.setImageResource(item.resource)

        imageView.setOnClickListener{
            listenerFunc?.invoke(position)
        }

        textView.setOnClickListener {
            listenerFunc?.invoke(position)
        }
    }
}

//inner class ViewHolderClass(rowBinding: RowBinding)
//    : RecyclerView.ViewHolder(rowBinding.root), View.OnClickListener {
//    // 항목 View 내부의 View 객체의 주소값을 담는다.
//    val rowImageView= rowBinding.rowImageView
//    val rowTextView = rowBinding.rowTextView
//
//    override fun onClick(p0: View?) {
//        val act= activity as MainActivity
//        val category_intent = Intent(act, CategoryActivity::class.java)
//        category_intent.putExtra("position",adapterPosition)
//        category_intent.putExtra("label",label)
//        act.startActivity(category_intent)
//    }
//}

//class CategoryViewHolder(
//    parent: ViewGroup,
//
//) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)){
//
//    private val imageView by lazy {
//        itemView.findViewById(R.id.rowImageView) as ImageView
//    }
//
//    private val textView by lazy {
//        itemView.findViewById(R.id.rowTextView) as TextView
//    }
//
//    fun onBind(item: ImageItem, position: Int) {
//        textView.text = item.title
//        imageView.setImageResource(item.resource)
//    }
//}