package kr.co.younhwan.happybuyer.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.category.CategoryRepository
import kr.co.younhwan.happybuyer.data.source.event.EventRepository
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
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class HomeFragment : Fragment(), HomeContract.View {
    private lateinit var viewDataBinding: FragmentHomeBinding

    private val homePresenter: HomePresenter by lazy {
        HomePresenter(
            this,
            categoryData = CategoryRepository,
            productData = ProductRepository,
            eventData = EventRepository,
            basketData = BasketRepository,
            homeAdapterModel = homeAdapter,
            homeAdapterView = homeAdapter,
            eventAdapterModel = eventAdapter,
            eventAdapterView = eventAdapter,
            popularAdapterModel = popularAdapter,
            popularAdapterView = popularAdapter,
        )
    }

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    private val eventAdapter: ProductAdapter by lazy {
        ProductAdapter("home")
    }

    private val popularAdapter: ProductAdapter by lazy {
        ProductAdapter("home")
    }

    private lateinit var categoryLabelList: ArrayList<String>

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

        // 엑티비티
        val act = activity as MainActivity

        // 카테고리, 행사 상품 및 인기 상품 로드
        homePresenter.loadCategories(true, requireContext())
        homePresenter.loadEventProduct(true)
        homePresenter.loadPopularProduct(true)

        // 전체 컨테이너
        OverScrollDecoratorHelper.setUpOverScroll(viewDataBinding.homeContentContainer)

        // 검색
        viewDataBinding.homeSearchContainer.setOnClickListener {
            act.startActivity(Intent(act, SearchActivity::class.java))
        }

        // 카테고리 리사이클러 뷰
        viewDataBinding.homeCategoryRecycler.adapter = homeAdapter
        viewDataBinding.homeCategoryRecycler.layoutManager =
            object : GridLayoutManager(context, 4) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = false
            }
        viewDataBinding.homeCategoryRecycler.addItemDecoration(homeAdapter.RecyclerDecoration())

        // 행사 상품 리사이클러 뷰
        viewDataBinding.homeEventRecycler.adapter = eventAdapter
        viewDataBinding.homeEventRecycler.layoutManager =
            object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollHorizontally() = true
                override fun canScrollVertically() = false
            }
        viewDataBinding.homeEventRecycler.addItemDecoration(eventAdapter.RecyclerDecoration())
        viewDataBinding.homeEventRecycler.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.homeEventRecycler,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )

        // 인기 상품 리사이클러 뷰
        viewDataBinding.homePopularRecycler.adapter = popularAdapter
        viewDataBinding.homePopularRecycler.layoutManager =
            object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollHorizontally() = true
                override fun canScrollVertically() = false
            }
        viewDataBinding.homePopularRecycler.addItemDecoration(popularAdapter.RecyclerDecoration())
        viewDataBinding.homePopularRecycler.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.homePopularRecycler,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )
    }

    override fun loadCategoriesCallback(list: ArrayList<CategoryItem>) {
        val temp = ArrayList<String>()

        for (index in list)
            temp.add(index.title)

        categoryLabelList = temp
    }

    override fun createCategoryActivity(adapterPosition: Int) {
        val act = activity as MainActivity
        val categoryIntent = Intent(act, CategoryActivity::class.java)
        categoryIntent.putExtra("init_position", adapterPosition)
        categoryIntent.putExtra("label", categoryLabelList)
        act.startActivity(categoryIntent)
    }

    override fun createProductActivity(productItem: ProductItem) {
        val act = activity as MainActivity
        val productIntent = Intent(act, ProductActivity::class.java)
        productIntent.putExtra("productItem", productItem)
        act.startActivity(productIntent)
    }

    override fun createLoginActivity() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun getAct() = activity as MainActivity

    override fun createProductInBasketResultCallback(count: Int) {
        when (count) {
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