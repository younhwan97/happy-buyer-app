package kr.co.younhwan.happybuyer.view.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

class SearchActivity : AppCompatActivity(), SearchContract.View {
    lateinit var viewDataBinding: ActivitySearchBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        val keyword = intent.getStringExtra("keyword") // 검색어

        searchPresenter.loadRecentSearch() // 최근 검색어를 불러온다.
        searchPresenter.loadSearchHistory() // 검색어 추천을 위해 (인기) 검색 기록을 불러온다.
        searchPresenter.loadResultSearch(keyword) // 검색 결과를 불러온다.

        // 컨테이너
        viewDataBinding.recentSearchContainer.visibility = View.GONE
        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
        viewDataBinding.searchResultContainer.visibility = View.GONE

        if (!keyword.isNullOrEmpty()) { // 검색어가 존재할 때
            viewDataBinding.searchResultContainer.visibility = View.VISIBLE // 검색 결과 컨테이너를 보여준다.
            viewDataBinding.searchViewInSearchToolbar.isIconified = true
            viewDataBinding.searchViewInSearchToolbar.setQuery(keyword, false)
        } else { // 검색어가 존재하지 않을 때
            viewDataBinding.recentSearchContainer.visibility = View.VISIBLE // 최근 검색 컨테이너를 보여준다.
            viewDataBinding.searchViewInSearchToolbar.isIconified = false
        }

        // 툴바
        viewDataBinding.searchViewInSearchToolbar.isIconifiedByDefault = false

        viewDataBinding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        viewDataBinding.searchToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.basketInSearchMenu -> {
                    createBasketActivity()
                    true
                }
                else -> false
            }
        }

        viewDataBinding.searchViewInSearchToolbar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        searchPresenter.onClickListenerOfKeyword(query)
                        searchPresenter.createRecentSearch(query) // 최근 검색어에 저장한다.
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    suggestedAdapter.filter.filter(newText) // 필터링
                    viewDataBinding.searchResultContainer.visibility = View.GONE
                    if (newText.isNullOrEmpty()) { // 검색어가 존재하지 않을 때
                        viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
                        viewDataBinding.suggestedSearchContainer.visibility = View.GONE
                    } else { // 검색어가 존재할 때
                        viewDataBinding.recentSearchContainer.visibility = View.GONE
                        viewDataBinding.suggestedSearchContainer.visibility = View.VISIBLE
                    }
                    return false
                }
            }
        )

        // 최근 검색
        viewDataBinding.recentSearchRecycler.adapter = recentAdapter
        viewDataBinding.recentSearchRecycler.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.recentSearchRecycler.addItemDecoration(recentAdapter.RecyclerDecoration())

        viewDataBinding.recentSearchDeleteAllBtn.setOnClickListener {
            searchPresenter.deleteAllRecentSearch()
        }

        // 추천 검색
        viewDataBinding.suggestedSearchRecycler.adapter = suggestedAdapter
        viewDataBinding.suggestedSearchRecycler.layoutManager = LinearLayoutManager(this)

        // 검색 결과
        viewDataBinding.searchResultRecycler.adapter = resultAdapter
        viewDataBinding.searchResultRecycler.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.searchResultRecycler.addItemDecoration(resultAdapter.RecyclerDecoration())

        viewDataBinding.searchResultSortingSpinner.setOnSpinnerItemSelectedListener<String> { _, oldItem, _, newItem ->
            if (oldItem != newItem && (!oldItem.isNullOrEmpty() || (oldItem.isNullOrEmpty() && newItem != "추천순"))) {
                viewDataBinding.searchResultLoadingView.visibility = View.VISIBLE
                viewDataBinding.searchResultRecycler.visibility = View.GONE
                viewDataBinding.searchResultLoadingImage.playAnimation()
                searchPresenter.sortSearchResult(newItem)
            }
        }
    }

    override fun loadSearchResultCallback(size: Int) {
        if (size == 0) { // 검색 결과가 없을 때
            viewDataBinding.searchResultEmptyView.visibility = View.VISIBLE
            viewDataBinding.searchResultRecycler.visibility = View.GONE
            viewDataBinding.searchResultCountText.visibility = View.GONE
            viewDataBinding.searchResultSortingSpinner.visibility = View.GONE
        } else { // 검색 결과가 있을 때
            viewDataBinding.searchResultEmptyView.visibility = View.GONE
            viewDataBinding.searchResultCountText.text = "총 ".plus(size.toString()).plus("개")
        }
    }

    override fun sortSearchResultCallback() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            viewDataBinding.searchResultLoadingView.visibility = View.GONE
            viewDataBinding.searchResultRecycler.visibility = View.VISIBLE
            viewDataBinding.searchResultLoadingImage.pauseAnimation()
        }, 500)
    }

    override fun getAct() = this

    override fun createResultActivity(keyword: String) {
        val resultIntent = Intent(this, SearchActivity::class.java)
        resultIntent.putExtra("keyword", keyword)
        finish() // 현재 엑티비티를 종료해 줘야 한다.
        startActivity(resultIntent)
    }

    override fun createProductActivity(productItem: ProductItem) {
        val productIntent = Intent(this, ProductActivity::class.java)
        productIntent.putExtra("productItem", productItem)
        startActivity(productIntent)
    }

    override fun createBasketActivity() {
        val basketIntent = Intent(this, BasketActivity::class.java)
        startActivity(basketIntent)
    }
}