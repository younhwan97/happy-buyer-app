package kr.co.younhwan.happybuyer.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.category.CategoryRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.FragmentHomeBinding
import kr.co.younhwan.happybuyer.view.category.CategoryActivity
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.home.adapter.main.HomeAdapter
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomeContract
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomePresenter
import kr.co.younhwan.happybuyer.view.product.ProductActivity
import kr.co.younhwan.happybuyer.view.search.SearchActivity

class HomeFragment : Fragment(), HomeContract.View {
    /* View Binding */
    private lateinit var viewDataBinding: FragmentHomeBinding

    /* Adapter */
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    private val eventAdapter: ProductAdapter by lazy {
        ProductAdapter("home")
    }

    private val popularAdapter: ProductAdapter by lazy {
        ProductAdapter("home")
    }

    /* Presenter */
    private val homePresenter: HomePresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        HomePresenter(
            this,
            categoryData = CategoryRepository,
            productData = ProductRepository,
            homeAdapterModel = homeAdapter,
            homeAdapterView = homeAdapter,
            eventAdapterModel = eventAdapter,
            eventAdapterView = eventAdapter,
            popularAdapterModel = popularAdapter,
            popularAdapterView = popularAdapter,
        )
    }

    /* Data */
    private lateinit var categoryLabelList: ArrayList<String>

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

        homePresenter.run {
            // load item by presenter
            loadCategories(requireContext(), false)
            loadEventProduct(false)
            loadPopularProduct(false)
        }

        viewDataBinding.run {
            // set item in data binding
            homeCategoryRecycler.run {
                adapter = homeAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
                addItemDecoration(homeAdapter.RecyclerDecoration())
            }

            homeEventRecycler.run {
                adapter = eventAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(eventAdapter.RecyclerDecoration())
            }

            homePopularRecycler.run {
                adapter = popularAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(popularAdapter.RecyclerDecoration())
            }

            homeSearchContainer.setOnClickListener {
                val act = activity as MainActivity
                act.startActivity(Intent(act, SearchActivity::class.java))
            }

            homeMoreBtn.setOnClickListener {

            }

            homeEventMoreBtn.setOnClickListener {
                createCategoryActivity(0)
            }

            homePopularMoreBtn.setOnClickListener {

            }
        }
    }

    override fun setCategoryLabelList(list: ArrayList<CategoryItem>) {
        val temp = ArrayList<String>()

        for (index in list)
            temp.add(index.title)

        categoryLabelList = temp
    }

    override fun createCategoryActivity(adapterPosition: Int) {
        val act = activity as MainActivity
        val categoryIntent = Intent(act, CategoryActivity::class.java)
        categoryIntent.putExtra("position", adapterPosition)
        categoryIntent.putExtra("label", categoryLabelList)
        act.startActivity(categoryIntent)
    }

    override fun createProductActivity(productItem: ProductItem){
        val act = activity as MainActivity
        val productIntent = Intent(act, ProductActivity::class.java)
        productIntent.putExtra("productItem", productItem)
        act.startActivity(productIntent)
    }

    override fun createLoginActivity() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun getAct() = activity as MainActivity

    override fun createProductInBasketResultCallback(count: Int) {
        when(count){
            0 -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }

            1 -> {
                Snackbar.make(viewDataBinding.root, "장바구니에 상품을 담았습니다.", Snackbar.LENGTH_SHORT)
            }

            20 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "같은 종류의 상품은 최대 20개까지 담을 수 있습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "한 번 더 담으셨네요! \n담긴 수량이 ${count}개가 되었습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }
        }.apply {
            setAnchorView(R.id.mainBottomNavigation)
            show()
        }
    }
}