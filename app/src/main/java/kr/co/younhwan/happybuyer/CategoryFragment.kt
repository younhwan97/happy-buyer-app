package kr.co.younhwan.happybuyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.item.view.*

class CategoryFragment:Fragment() {
    var imgRes = arrayOf(
        R.drawable.apple
    )

    var testName = arrayOf(
       "사과"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_category, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = RecyclerAdapter()
        itemContainer.adapter = adapter1

        itemContainer.layoutManager = GridLayoutManager(activity as CategoryActivity, 2)
    }

    // Recycler view
    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>(){
        // 항목 구성을 위한 사용할 ViewHolder 객체가 필요할 때 사용하는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val itemView = layoutInflater.inflate(R.layout.item, null)
            val holder = ViewHolderClass(itemView)
            return holder
        }
        // ViewHolder를 통해 항목을 구성할 때 항목 내의 View 객체에 데이터를 세팅한다.
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.itemImage.setImageResource(imgRes[position])
            holder.itemName.text = testName[position]
        }

        override fun getItemCount(): Int {
            return testName.size
        }

        // ViewHolder 클래스
        inner class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
            val itemName = itemView.itemName
            val itemImage = itemView.itemImage
        }
    }

}