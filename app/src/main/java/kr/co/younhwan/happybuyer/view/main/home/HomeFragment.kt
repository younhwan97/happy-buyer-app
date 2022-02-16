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
import kr.co.younhwan.happybuyer.view.main.home.adapter.HomeAdapter
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
            adapterModel = homeAdapter,
            adapterView = homeAdapter
        )
    }

    /* Adapter */
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    /* Data */
    private lateinit var categoryLabel: ArrayList<String>

    /* Method */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentHomeBinding.inflate(inflater) // Data Binding
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load item by presenter
        homePresenter.loadItems(requireContext(), false)

        // set recycler view
        viewDataBinding.recycler.run {
            adapter = homeAdapter
            layoutManager = GridLayoutManager(requireContext(), 5)
            addItemDecoration(homeAdapter.RecyclerDecoration(20))
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