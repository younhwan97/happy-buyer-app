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
import kr.co.younhwan.happybuyer.GlobalApplication
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

        // ????????????, ?????? ?????? ??? ?????? ?????? ??????
        homePresenter.loadCategories(true, requireContext())
        homePresenter.loadEventProducts(true)
        homePresenter.loadPopularProducts(true)

        // ?????? ????????????
        OverScrollDecoratorHelper.setUpOverScroll(viewDataBinding.homeContentContainer)

        // ??????
        viewDataBinding.homeSearchContainer.setOnClickListener {
            val act = activity as MainActivity
            act.startActivity(Intent(act, SearchActivity::class.java))
        }

        // ???????????? ??????????????????
        viewDataBinding.homeCategoryRecycler.adapter = homeAdapter
        viewDataBinding.homeCategoryRecycler.layoutManager =
            object : GridLayoutManager(context, 4) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = false
            }

        viewDataBinding.homeCategoryRecycler.addItemDecoration(homeAdapter.RecyclerDecoration())

        // ?????? ?????? ??????????????????
        viewDataBinding.homeEventRecycler.adapter = eventAdapter
        viewDataBinding.homeEventRecycler.layoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally() = true
                override fun canScrollVertically() = false
            }

        viewDataBinding.homeEventRecycler.addItemDecoration(eventAdapter.RecyclerDecoration())
        viewDataBinding.homeEventRecycler.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.homeEventRecycler,
            OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
        )

        viewDataBinding.homeEventMoreBtn.setOnClickListener {
            createCategoryAct(0)
        }

        // ?????? ?????? ??????????????????
        viewDataBinding.homePopularRecycler.adapter = popularAdapter
        viewDataBinding.homePopularRecycler.layoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false) {
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

    override fun getAct() = activity as MainActivity

    override fun loadCategoriesCallback(list: ArrayList<CategoryItem>) {
        val temp = ArrayList<String>()

        for (index in list)
            temp.add(index.title)

        categoryLabelList = temp
    }

    override fun loadEventProductsCallback(resultCount: Int) {
        viewDataBinding.homeEventContainer.visibility =
            if (resultCount == 0) View.GONE else View.VISIBLE
    }

    override fun loadPopularProductsCallback(resultCount: Int) {
        viewDataBinding.homePopularContainer.visibility =
            if (resultCount == 0) View.GONE else View.VISIBLE
    }

    override fun createCategoryAct(adapterPosition: Int) {
        val categoryIntent = Intent(context, CategoryActivity::class.java)
        categoryIntent.putExtra("init_position", adapterPosition)
        categoryIntent.putExtra("label", categoryLabelList)
        startActivity(categoryIntent)
    }

    override fun createProductAct(productItem: ProductItem) {
        val productIntent = Intent(context, ProductActivity::class.java)
        productIntent.putExtra("product", productItem)
        startActivity(productIntent)
    }

    override fun createLoginAct() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun createOrUpdateProductInBasketCallback(resultCount: Int) {
        val snack = when (resultCount) {
            1 -> {
                // ????????? ????????? ???????????? ???????????? ?????? ??????????????? ???
                val act = activity as MainActivity
                val app = act.application as GlobalApplication

                app.basketItemCount += 1

                if (app.basketItemCount == 1) {
                    act.setNotificationBadge()
                }

                // ????????? ??????
                Snackbar.make(
                    viewDataBinding.root,
                    "??????????????? ????????? ???????????????.",
                    Snackbar.LENGTH_SHORT
                )
            }

            in 2..19 -> {
                // ????????? ??????????????? ????????? ????????? ????????? ????????? ???
                // ????????? ??????
                Snackbar.make(
                    viewDataBinding.root,
                    "??? ??? ??? ???????????????! \n?????? ????????? ${resultCount}?????? ???????????????.",
                    Snackbar.LENGTH_SHORT
                )
            }

            20 -> {
                // ????????? ??????
                Snackbar.make(
                    viewDataBinding.root,
                    "?????? ????????? ????????? ?????? 20????????? ?????? ??? ????????????.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                // ????????? ??????
                Snackbar.make(
                    viewDataBinding.root,
                    "??? ??? ?????? ????????? ??????????????????.",
                    Snackbar.LENGTH_SHORT
                )
            }
        }

        snack.setAnchorView(R.id.mainBottomNavigation)
        snack.show()
    }
}