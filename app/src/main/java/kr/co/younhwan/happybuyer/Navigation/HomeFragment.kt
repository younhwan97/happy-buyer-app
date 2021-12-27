package kr.co.younhwan.happybuyer.Navigation

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.row.view.*
import kr.co.younhwan.happybuyer.CategoryActivity
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R

class HomeFragment : Fragment() {
    // setting data to use
    var imgRes = intArrayOf(
        R.drawable.category_fruit, R.drawable.category_meat, R.drawable.category_vegetable, R.drawable.category_milk, R.drawable.category_kimchi,
        R.drawable.category_fish, R.drawable.category_water, R.drawable.category_coffee,   R.drawable.category_chips, R.drawable.category_frozen,
        R.drawable.category_ramen, R.drawable.category_seasoning, R.drawable.category_rice, R.drawable.category_cleaning, R.drawable.category_tissue,
        R.drawable.category_kitchen, R.drawable.category_pet
    )

    var label = arrayOf(
        "과일", "정육", "채소", "우유/유제품", "김치/반찬",
        "수산/건해산", "생수/음료", "커피/차", "과자/빙과", "냉장/냉동식품",
        "라면/즉석식품", "장/양념", "쌀/잡곡", "세탁/청소", "제지/위생",
        "주방", "반려동물"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = RecyclerAdapter()
        recycler.adapter = adapter1
        recycler.layoutManager = GridLayoutManager(activity as MainActivity, 5)
        recycler.addItemDecoration(RecyclerDecoration(20))
    }

    // RecyclerView의 어댑터 클래스
    inner class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {

        // 항목 구성을 위해 사용할 ViewHolder 객체가 필요할 때 호출되는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            // 항목으로 사용할 view 객체를 생성
            val itemView = layoutInflater.inflate(R.layout.row,null)
            val holder = ViewHolderClass(itemView)
            itemView.setOnClickListener(holder)
            return holder
        }

        // ViewHolder를 통해 항목을 구성할 때 항목 내의 View 객체에 데이터를 세팅
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowImageView.setImageResource(imgRes[position])
            holder.rowTextView.text = label[position]
        }

        override fun getItemCount(): Int {
            return label.size
        }

        // ViewHolder 클래스
        inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            // 항목 View 내부의 View 객체의 주소값을 담는다.
            val rowImageView= itemView.rowImageView
            val rowTextView = itemView.rowTextView

            override fun onClick(p0: View?) {
                val Activity = activity as MainActivity
                var category_intent = Intent(Activity, CategoryActivity::class.java)
                category_intent.putExtra("position",adapterPosition)
                category_intent.putExtra("label",label)
                Activity.startActivity(category_intent)
            }
        }
    }

    // RecyclerView의 간격을 조정하기 위한 클래스
    inner class RecyclerDecoration(private val divHeight : Int) :RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom= divHeight
        }
    }
}