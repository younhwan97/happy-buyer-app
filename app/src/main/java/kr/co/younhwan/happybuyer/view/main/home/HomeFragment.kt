package kr.co.younhwan.happybuyer.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.younhwan.happybuyer.data.ImageItem
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.databinding.FragmentHomeBinding
import kr.co.younhwan.happybuyer.view.category.CategoryActivity
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.home.adapter.CategoryAdapter
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomeContract
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomePresenter

/**
 * View
 */

class HomeFragment : Fragment(), HomeContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentHomeBinding

    /* Presenter */
    private val homePresenter: HomePresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        HomePresenter(
            this,
            imageData = SampleImageRepository,
            adapterModel = categoryAdapter,
            adapterView = categoryAdapter
        )
    }

    /* Adapter */
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter()
    }

    /* Data */
    private lateinit var categoryLabel: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentHomeBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePresenter.loadItems(requireContext(), false)
        viewDataBinding.recycler.run {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 5)
            addItemDecoration(categoryAdapter.RecyclerDecoration(20))
        }
    }

    override fun setCategoryLabel(list: ArrayList<ImageItem>) {
        val temp = ArrayList<String>()

        for (index in list)
            temp.add(index.title)

        categoryLabel = temp
    }

    override fun createCategoryActivity(adapterPosition: Int) {
        val act = activity as MainActivity
        val categoryIntent = Intent(act, CategoryActivity::class.java)
        categoryIntent.putExtra("position", adapterPosition)
        categoryIntent.putExtra("label", categoryLabel)
        act.startActivity(categoryIntent)
    }
}


//override fun updateCategory(image: ArrayList<Int>, label: ArrayList<String>) {
//    this.imgRes = image
//    this.label = label
//}
//
//override fun notifyAdapter() {
//    RecyclerAdapter().notifyAdapter()
//}
//
//
//// RecyclerView Adapter Class
//inner class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
//        // View를 생성한다.
//        val rowBinding = RowBinding.inflate(layoutInflater)
//        val holder = ViewHolderClass(rowBinding)
//        rowBinding.root.setOnClickListener(holder)
//
//        return holder
//    }
//
//    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
//        // View를 bind 한다.
//        holder.rowImageView.setImageResource(imgRes[position])
//        holder.rowTextView.text = label[position]
//    }
//
//    override fun getItemCount(): Int {
//        //  현재 View에 노출할 아이템의 카운트 수
//        return label.size
//    }
//
//    fun notifyAdapter(){
//        notifyDataSetChanged()
//    }
//
//    // ViewHolder 클래스
//    inner class ViewHolderClass(rowBinding: RowBinding)
//        : RecyclerView.ViewHolder(rowBinding.root), View.OnClickListener {
//        // 항목 View 내부의 View 객체의 주소값을 담는다.
//        val rowImageView= rowBinding.rowImageView
//        val rowTextView = rowBinding.rowTextView
//
//        override fun onClick(p0: View?) {
//            val act= activity as MainActivity
//            val category_intent = Intent(act, CategoryActivity::class.java)
//            category_intent.putExtra("position",adapterPosition)
//            category_intent.putExtra("label",label)
//            act.startActivity(category_intent)
//        }
//    }
//}
//
//// RecyclerView의 간격을 조정하기 위한 클래스
//inner class RecyclerDecoration(private val divHeight : Int) :RecyclerView.ItemDecoration() {
//    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
//        outRect.bottom= divHeight
//    }
//}