package kr.co.younhwan.happybuyer.view.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.search.SearchRepository
import kr.co.younhwan.happybuyer.databinding.ActivitySearchBinding
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

            searchViewInSearchToolbar.run {
                isIconifiedByDefault = false
                suggestedSearchContainer.visibility = View.GONE

                if(keyword.isNullOrBlank()){
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
            allRecentSearchDeleteBtn.setOnClickListener {
                searchPresenter.deleteAllRecentSearch()
            }

            recentSearchRecycler.run {
                adapter = recentAdapter
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(recentAdapter.RecyclerDecoration())
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
        }
    }

    override fun getAct() = this

    override fun createResultActivity(keyword: String) {
        val resultIntent = Intent(this, SearchActivity:: class.java)
        resultIntent.putExtra("keyword", keyword)
        finish()
        startActivity(resultIntent)
    }
}