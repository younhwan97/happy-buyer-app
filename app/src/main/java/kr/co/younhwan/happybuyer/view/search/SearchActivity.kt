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

        // 인텐트에서 데이터 추출
        val keyword = intent.getStringExtra("keyword") // 검색어

        // 데이터 로드
        nowPage = 1
        searchPresenter.loadRecent() // 최근 검색어
        searchPresenter.loadSearchHistory() // 검색어 추천을 위한 검색 기록
        searchPresenter.loadSearchResult(true, keyword, null, nowPage) // 검색 결과

        // 전체 컨테이너
        viewDataBinding.recentSearchContainer.visibility = View.GONE
        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
        viewDataBinding.searchResultContainer.visibility = View.GONE

        // 툴바 & 메뉴
        if (keyword.isNullOrEmpty() || keyword.isNullOrBlank()) {
            // 검색어가 존재하지 않을 때
            viewDataBinding.searchViewInSearchToolbar.isIconified = false // focusing

            // 최근 검색 컨테이너를 보여준다.
            viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
        } else {
            // 검색어가 존재할 때
            viewDataBinding.searchViewInSearchToolbar.isIconified = true // focusing
            viewDataBinding.searchViewInSearchToolbar.setQuery(keyword, false)

            // 검색 결과 컨테이너를 보여주고, 로딩 뷰 셋팅
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
                        searchPresenter.createRecentWithHistory(query) // 최근 검색어에 저장한다.
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    suggestedAdapter.filter.filter(newText) // 필터링
                    viewDataBinding.searchResultContainer.visibility = View.GONE

                    if (newText.isNullOrEmpty() || newText.isNullOrBlank()) {
                        // 검색어가 존재하지 않을 때 -> 최근 검색어 제시
                        viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
                        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
                    } else {
                        // 검색어가 존재할 때 -> 추천 검색어 제시
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

        // 최근 검색
        viewDataBinding.recentSearchRecycler.adapter = recentAdapter
        viewDataBinding.recentSearchRecycler.layoutManager = object : GridLayoutManager(this, 2) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }

        viewDataBinding.recentSearchRecycler.addItemDecoration(recentAdapter.RecyclerDecoration())

        viewDataBinding.recentSearchDeleteAllBtn.setOnClickListener {
            searchPresenter.deleteAllRecent()
        }

        // 추천 검색
        viewDataBinding.suggestedSearchRecycler.adapter = suggestedAdapter
        viewDataBinding.suggestedSearchRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }

        // 검색 결과
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

                // 무한스크롤
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

        setSortingOption("추천순")
        viewDataBinding.searchResultSortingSpinner.lifecycleOwner = this // 메모리 누수 방지
        viewDataBinding.searchResultSortingSpinner.setOnSpinnerItemSelectedListener<String> { _, oldItem, _, newItem ->
            if (oldItem != newItem) {
                // 로딩뷰 셋팅
                viewDataBinding.searchResultLoadingView.visibility = View.VISIBLE
                viewDataBinding.searchResultLoadingImage.playAnimation()
                viewDataBinding.searchResultRecycler.visibility = View.GONE

                // 새로운 정렬 기준 저장
                setSortingOption(newItem)

                // 새로운 정렬 기준에 따라 검색 결과 로드
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
            "판매순" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(1)
                selectedSortingOption = "판매순"
            }

            "낮은 가격순" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(2)
                selectedSortingOption = "낮은 가격순"
            }

            "높은 가격순" -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(3)
                selectedSortingOption = "높은 가격순"
            }

            else -> {
                viewDataBinding.searchResultSortingSpinner.selectItemByIndex(0)
                selectedSortingOption = "추천순"
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
                // 키워드를 포함한 상품이 하나도 없을 때
                viewDataBinding.searchResultEmptyView.visibility = View.VISIBLE
            }
            // 더이상 로드할 상품이 없을 때
            nowPage = -1
        } else {
            // 검색 결과가 있을 때 -> recycler view
            viewDataBinding.searchResultTopContainer.visibility = View.VISIBLE
            viewDataBinding.searchResultRecycler.visibility = View.VISIBLE
        }

        // 로딩 뷰 종료
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