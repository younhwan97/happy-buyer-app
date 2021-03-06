package kr.co.younhwan.happybuyer.view.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.search.SearchRepository
import kr.co.younhwan.happybuyer.databinding.ActivitySearchBinding
import kr.co.younhwan.happybuyer.view.basket.BasketActivity
import kr.co.younhwan.happybuyer.view.product.ProductActivity
import kr.co.younhwan.happybuyer.view.search.adapter.recent.RecentAdapter
import kr.co.younhwan.happybuyer.view.search.adapter.suggested.SuggestedAdapter
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class SearchActivity : AppCompatActivity(), SearchContract.View {
    lateinit var viewDataBinding: ActivitySearchBinding

    private val searchPresenter: SearchPresenter by lazy {
        SearchPresenter(
            view = this,
            searchData = SearchRepository,
            productData = ProductRepository,
            recentAdapterView = recentAdapter,
            recentAdapterModel = recentAdapter,
            suggestedAdapterView = suggestedAdapter,
            suggestedAdapterModel = suggestedAdapter,
            resultAdapterView = resultAdapter,
            resultAdapterModel = resultAdapter
        )
    }

    private val recentAdapter: RecentAdapter by lazy {
        RecentAdapter()
    }

    private val suggestedAdapter: SuggestedAdapter by lazy {
        SuggestedAdapter()
    }

    private val resultAdapter: ProductAdapter by lazy {
        ProductAdapter(
            usingBy = "search"
        )
    }

    private var nowPage = 1
    private lateinit var selectedSortingOption: String
    private lateinit var notificationBadgeOfBasketMenu: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // ??????????????? ????????? ??????
        val keyword = intent.getStringExtra("keyword") // ?????????

        // ????????? ??????
        nowPage = 1
        searchPresenter.loadRecent() // ?????? ?????????
        searchPresenter.loadSearchHistory() // ????????? ????????? ?????? ?????? ??????
        searchPresenter.loadSearchResult(true, keyword, null, nowPage) // ?????? ??????

        // ?????? ????????????
        viewDataBinding.recentSearchContainer.visibility = View.GONE
        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
        viewDataBinding.searchResultContainer.visibility = View.GONE

        // ?????? & ??????
        if (keyword.isNullOrEmpty() || keyword.isNullOrBlank()) {
            // ???????????? ???????????? ?????? ???
            viewDataBinding.searchViewInSearchToolbar.isIconified = false // focusing

            // ?????? ?????? ??????????????? ????????????.
            viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
        } else {
            // ???????????? ????????? ???
            viewDataBinding.searchViewInSearchToolbar.isIconified = true // focusing
            viewDataBinding.searchViewInSearchToolbar.setQuery(keyword, false)

            // ?????? ?????? ??????????????? ????????????, ?????? ??? ??????
            viewDataBinding.searchResultContainer.visibility = View.VISIBLE
            viewDataBinding.searchResultTopContainer.visibility = View.GONE
            viewDataBinding.searchResultRecycler.visibility = View.GONE
            viewDataBinding.searchResultEmptyView.visibility = View.GONE
            viewDataBinding.searchResultLoadingView.visibility = View.VISIBLE
            viewDataBinding.searchResultLoadingImage.playAnimation()
        }

        viewDataBinding.searchViewInSearchToolbar.isIconifiedByDefault = false

        viewDataBinding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        viewDataBinding.searchViewInSearchToolbar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty() && !query.isNullOrBlank()) {
                        searchPresenter.onClickListenerOfKeyword(query)
                        searchPresenter.createRecentWithHistory(query) // ?????? ???????????? ????????????.
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    suggestedAdapter.filter.filter(newText) // ?????????
                    viewDataBinding.searchResultContainer.visibility = View.GONE

                    if (newText.isNullOrEmpty() || newText.isNullOrBlank()) {
                        // ???????????? ???????????? ?????? ??? -> ?????? ????????? ??????
                        viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
                        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
                    } else {
                        // ???????????? ????????? ??? -> ?????? ????????? ??????
                        viewDataBinding.recentSearchContainer.visibility = View.GONE
                        viewDataBinding.suggestedSearchContainer.visibility = View.VISIBLE
                    }
                    return false
                }
            }
        )

        val menu = viewDataBinding.searchToolbar.menu
        val basketItem = menu.findItem(R.id.basketInSearch)
        val actionView = basketItem.actionView

        actionView.setOnClickListener {
            val basketIntent = Intent(this, BasketActivity::class.java)
            startActivity(basketIntent)
        }

        notificationBadgeOfBasketMenu = actionView.findViewById(R.id.cart_badge)
        setNotificationBadge()

        // ?????? ??????
        viewDataBinding.recentSearchRecycler.adapter = recentAdapter
        viewDataBinding.recentSearchRecycler.layoutManager = object : GridLayoutManager(this, 2) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }

        viewDataBinding.recentSearchRecycler.addItemDecoration(recentAdapter.RecyclerDecoration())

        viewDataBinding.recentSearchDeleteAllBtn.setOnClickListener {
            searchPresenter.deleteAllRecent()
        }

        // ?????? ??????
        viewDataBinding.suggestedSearchRecycler.adapter = suggestedAdapter
        viewDataBinding.suggestedSearchRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }

        // ?????? ??????
        viewDataBinding.searchResultRecycler.adapter = resultAdapter
        viewDataBinding.searchResultRecycler.layoutManager = object : GridLayoutManager(this, 2) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }

        viewDataBinding.searchResultRecycler.addItemDecoration(resultAdapter.RecyclerDecoration())

        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.searchResultRecycler,
            OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )

        viewDataBinding.searchResultRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // ???????????????
                if (!recyclerView.canScrollVertically(1)) {
                    if (nowPage != -1) {
                        nowPage += 1
                        searchPresenter.loadMoreSearchResult(
                            keyword = keyword,
                            page = nowPage,
                            sortBy = selectedSortingOption
                        )
                    }
                }
            }
        })

        setSortingOption("?????????")
        viewDataBinding.searchResultSortingSpinner.lifecycleOwner = this // ????????? ?????? ??????
        viewDataBinding.searchResultSortingSpinner.setOnSpinnerItemSelectedListener<String> { _, oldItem, _, newItem ->
            if (oldItem != newItem) {
                // ????????? ??????
                viewDataBinding.searchResultLoadingView.visibility = View.VISIBLE
                viewDataBinding.searchResultLoadingImage.playAnimation()
                viewDataBinding.searchResultRecycler.visibility = View.GONE

                // ????????? ?????? ?????? ??????
                setSortingOption(newItem)

                // ????????? ?????? ????????? ?????? ?????? ?????? ??????
                nowPage = 1
                searchPresenter.loadSearchResult(
                    isClear = true,
                    keyword = keyword,
                    sortBy = selectedSortingOption,
                    page = nowPage
                )
            }
        }
    }

    private fun setSortingOption(sortBy: String) {
        when (sortBy) {
            "?????????" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(1)
                selectedSortingOption = "?????????"
            }

            "?????? ?????????" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(2)
                selectedSortingOption = "?????? ?????????"
            }

            "?????? ?????????" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(3)
                selectedSortingOption = "?????? ?????????"
            }

            else -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(0)
                selectedSortingOption = "?????????"
            }
        }
    }

    private fun setNotificationBadge() {
        notificationBadgeOfBasketMenu.visibility =
            if ((application as GlobalApplication).basketItemCount > 0) View.VISIBLE else View.GONE
    }

    override fun getAct() = this

    override fun loadSearchResultCallback(resultCount: Int) {
        if (resultCount == 0) {
            if (nowPage == 1) {
                // ???????????? ????????? ????????? ????????? ?????? ???
                viewDataBinding.searchResultEmptyView.visibility = View.VISIBLE
            }
            // ????????? ????????? ????????? ?????? ???
            nowPage = -1
        } else {
            // ?????? ????????? ?????? ??? -> recycler view
            viewDataBinding.searchResultTopContainer.visibility = View.VISIBLE
            viewDataBinding.searchResultRecycler.visibility = View.VISIBLE
        }

        // ?????? ??? ??????
        viewDataBinding.searchResultLoadingView.visibility = View.GONE
        viewDataBinding.searchResultLoadingImage.pauseAnimation()
    }

    override fun createResultActivity(keyword: String) {
        val resultIntent = Intent(this, SearchActivity::class.java)
        resultIntent.putExtra("keyword", keyword)
        finish()
        startActivity(resultIntent)
    }

    override fun createProductActivity(productItem: ProductItem) {
        val productIntent = Intent(this, ProductActivity::class.java)
        productIntent.putExtra("productItem", productItem)
        startActivity(productIntent)
    }
}