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

        val keyword = intent.getStringExtra("keyword")

        searchPresenter.loadRecentSearch() // 최근 검색어를 불러온다.
        searchPresenter.loadSearchHistory() // 검색어 추천을 위해 (인기) 검색 기록을 불러온다.
        searchPresenter.loadResultSearch(keyword) // 검색 결과를 불러온다.

        viewDataBinding.run {
            /* Toolbar */
            searchToolbar.setNavigationOnClickListener {
                finish()
            }

            searchToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.basketInSearchMenu -> {
                        createBasketActivity()
                        true
                    }
                    else -> false
                }
            }

            searchViewInSearchToolbar.run {
                isIconifiedByDefault = false
                suggestedSearchContainer.visibility = View.GONE

                if (keyword.isNullOrBlank()) {
                    isIconified = false // focusing (= show keypad)

                    recentSearchContainer.visibility = View.VISIBLE
                    resultSearchContainer.visibility = View.GONE
                } else {
                    isIconified = true // not focusing (= hide keypad)
                    setQuery(keyword, false)

                    recentSearchContainer.visibility = View.GONE
                    resultSearchContainer.visibility = View.VISIBLE
                }

                setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (!query.isNullOrEmpty()) {
                                searchPresenter.onClickListenerOfKeyword(query)
                                searchPresenter.createRecentSearch(query) // 최근 검색어에 저장한다.
                            }
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText.isNullOrEmpty()) { // 입력된 단어가 없다면 최근 검색어 화면을 보여준다.
                                recentSearchContainer.visibility = View.VISIBLE
                                suggestedSearchContainer.visibility = View.GONE
                            } else { // 추천 검색어 화면을 보여준다.
                                recentSearchContainer.visibility = View.GONE
                                suggestedSearchContainer.visibility = View.VISIBLE
                            }
                            resultSearchContainer.visibility = View.GONE

                            suggestedAdapter.filter.filter(newText) // 필터링
                            return false
                        }
                    })
            }

            /* Recent Search */
            recentSearchRecycler.run {
                adapter = recentAdapter
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(recentAdapter.RecyclerDecoration())
            }

            allRecentSearchDeleteBtn.setOnClickListener {
                searchPresenter.deleteAllRecentSearch()
            }

            /* Suggested Search */
            suggestedSearchRecycler.run {
                adapter = suggestedAdapter
                layoutManager = LinearLayoutManager(context)
            }

            /* Result Search */
            resultSearchRecycler.run {
                adapter = resultAdapter
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(resultAdapter.RecyclerDecoration())
            }

            resultSortSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                if(oldItem.isNullOrBlank()){
                    if(newItem != "추천순"){
                        resultSearchLoadingView.visibility = View.VISIBLE
                        resultSearchRecycler.visibility = View.GONE
                        loadingImage.playAnimation()
                        searchPresenter.sortResultSearch(newItem)
                    }
                } else{
                    if(oldItem != newItem){
                        resultSearchLoadingView.visibility = View.VISIBLE
                        resultSearchRecycler.visibility = View.GONE
                        loadingImage.playAnimation()
                        searchPresenter.sortResultSearch(newItem)
                    }
                }
            }

            resultSearchLoadingView.visibility = View.GONE
        }
    }

    override fun getAct() = this

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

    override fun createBasketActivity() {
        val basketIntent = Intent(this, BasketActivity::class.java)
        startActivity(basketIntent)
    }

    override fun loadResultSearchCallback(size: Int) {
        if (size == 0) { // 검색 결과가 없을 때 (= empty view 를 visible 상태로 만든다.)
            viewDataBinding.resultSearchEmptyView.visibility = View.VISIBLE
            viewDataBinding.resultSearchRecycler.visibility = View.GONE
            viewDataBinding.resultItemCountText.visibility = View.GONE
            viewDataBinding.resultSortSpinner.visibility = View.GONE
        } else {
            viewDataBinding.resultSearchEmptyView.visibility = View.GONE
            viewDataBinding.recentSearchRecycler.visibility = View.VISIBLE

            viewDataBinding.resultItemCountText.text = "총 ".plus(size.toString()).plus("개")
        }
    }

    override fun sortResultSearchCallback() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            viewDataBinding.resultSearchLoadingView.visibility = View.GONE
            viewDataBinding.resultSearchRecycler.visibility = View.VISIBLE
            viewDataBinding.loadingImage.pauseAnimation()
        }, 500)
    }
}