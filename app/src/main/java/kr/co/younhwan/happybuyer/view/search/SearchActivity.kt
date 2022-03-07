package kr.co.younhwan.happybuyer.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.data.source.search.SearchRepository
import kr.co.younhwan.happybuyer.databinding.ActivitySearchBinding
import kr.co.younhwan.happybuyer.view.search.adapter.recent.RecentAdapter
import kr.co.younhwan.happybuyer.view.search.adapter.suggested.SuggestedAdapter

class SearchActivity : AppCompatActivity(), SearchContract.View {
    lateinit var viewDataBinding: ActivitySearchBinding

    private val recentAdapter : RecentAdapter by lazy {
        RecentAdapter()
    }

    private val suggestedAdapter: SuggestedAdapter by lazy {
        SuggestedAdapter()
    }

    private val searchPresenter : SearchPresenter by lazy {
        SearchPresenter(
            view = this,
            searchData = SearchRepository,
            recentAdapterView = recentAdapter,
            recentAdapterModel = recentAdapter,
            suggestedAdapterView = suggestedAdapter,
            suggestedAdapterModel = suggestedAdapter
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        searchPresenter.loadRecentSearch()  // 최근 검색어를 불러온다.
        searchPresenter.loadSearchHistory() // 검색어 추천을 위해 검색 기록을 불러온다.

        viewDataBinding.run {
            /* Toolbar */
            searchToolbar.setNavigationOnClickListener {
                finish()
            }

            searchViewInSearchToolbar.run {
                isIconified = false // focus
                isIconifiedByDefault = false
                setOnQueryTextListener(queryTextListener)
            }

            /* Recent Search */
            recentSearchContainer.visibility = View.VISIBLE

            allRecentSearchDeleteBtn.setOnClickListener {
                searchPresenter.deleteAllRecentSearch()
            }

            recentSearchRecycler.run {
                adapter = recentAdapter
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(recentAdapter.RecyclerDecoration())
            }

            /* Suggested Search */
            suggestedSearchContainer.visibility = View.GONE

            suggestedSearchRecycler.run {
                adapter = suggestedAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun getAct() = this

    private val queryTextListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {

            if(!query.isNullOrEmpty()){
                searchPresenter.createRecentSearch(query) // 최근 검색어에 저장한다.
            }

            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if(newText.isNullOrEmpty()){
                viewDataBinding.recentSearchContainer.visibility = View.VISIBLE
                viewDataBinding.suggestedSearchContainer.visibility = View.GONE
            }else{
                viewDataBinding.recentSearchContainer.visibility = View.GONE
                viewDataBinding.suggestedSearchContainer.visibility = View.VISIBLE
            }

            suggestedAdapter.filter.filter(newText)
            return false
        }
    }
}