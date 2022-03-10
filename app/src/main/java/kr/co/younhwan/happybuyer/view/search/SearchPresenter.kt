package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.SearchItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.search.SearchRepository
import kr.co.younhwan.happybuyer.data.source.search.SearchSource
import kr.co.younhwan.happybuyer.view.search.adapter.recent.contract.RecentAdapterContract
import kr.co.younhwan.happybuyer.view.search.adapter.suggested.contract.SuggestedAdapterContract

class SearchPresenter(
    private val view: SearchContract.View,
    private val searchData: SearchRepository,
    private val productData: ProductRepository,
    private val recentAdapterView: RecentAdapterContract.View,
    private val recentAdapterModel: RecentAdapterContract.Model,
    private val suggestedAdapterView: SuggestedAdapterContract.View,
    private val suggestedAdapterModel: SuggestedAdapterContract.Model,
    private val resultAdapterView: ProductAdapterContract.View,
    private val resultAdapterModel: ProductAdapterContract.Model
) : SearchContract.Model {

    init {
        recentAdapterView.onClickFuncOfDeleteBtn = { keyword: String, i: Int ->
            onClickListenerOfDeleteBtn(keyword, i)
        }

        recentAdapterView.onClickFuncOfRecentSearch = {
            onClickListenerOfKeyword(it)
        }

        suggestedAdapterView.onClickFuncOfSuggestedSearch = {
            onClickListenerOfKeyword(it)
        }
    }

    val app = view.getAct().application as GlobalApplication

    override fun createRecentSearch(keyword: String) {
        if (app.isLogined) { // 로그인 상태에서만 검색어를 저장

            var alreadyExistsKeyword = false
            for (i in 0 until recentAdapterModel.getItemCount()) {
                if (recentAdapterModel.getItem(i).keyword == keyword) {
                    alreadyExistsKeyword = true
                    break
                }
            }

            if (!alreadyExistsKeyword) {
                searchData.createRecentSearch(
                    kakaoAccountId = app.kakaoAccountId,
                    keyword = keyword
                )
            }
        }
    }

    override fun loadRecentSearch() {
        if (app.isLogined) {
            searchData.readRecentSearch(
                kakaoAccountId = app.kakaoAccountId,
                readRecentSearchCallback = object : SearchSource.ReadRecentSearchCallback {
                    override fun onReadRecentSearch(list: ArrayList<SearchItem>) {
                        // list 가 비어있더라도 addItems 와 notifyAdapter 를 호출해야 에러가 발생하지 않는다.
                        recentAdapterModel.addItems(list)
                        recentAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            recentAdapterModel.addItems(ArrayList<SearchItem>())
            recentAdapterView.notifyAdapter()
        }
    }

    override fun deleteAllRecentSearch() {
        if (app.isLogined) {
            // 저장된 검색어가 있는 경우 (불필요한 api 호출 방지)
            if (recentAdapterModel.getItemCount() != 0) {
                // keyword == null -> 모든 최근 검색어 삭제
                // keyword !== null -> keyword 에 해당하는 검색어만 삭제
                searchData.deleteRecentSearch(
                    kakaoAccountId = app.kakaoAccountId,
                    keyword = null,
                    deleteRecentSearchCallback = object : SearchSource.DeleteRecentSearchCallback {
                        override fun onDeleteRecentSearch(isSuccess: Boolean) {
                            if (isSuccess) {
                                recentAdapterModel.clearItem()
                                recentAdapterView.notifyAdapter()
                            }
                        }
                    }
                )
            }
        }
    }

    private fun onClickListenerOfDeleteBtn(keyword: String, position: Int) {
        if (app.isLogined) {
            // keyword == null -> 모든 최근 검색어 삭제
            // keyword !== null -> keyword 에 해당하는 검색어만 삭제
            searchData.deleteRecentSearch(
                kakaoAccountId = app.kakaoAccountId,
                keyword = keyword,
                deleteRecentSearchCallback = object : SearchSource.DeleteRecentSearchCallback {
                    override fun onDeleteRecentSearch(isSuccess: Boolean) {
                        if (isSuccess) {
                            recentAdapterModel.deleteItem(position)
                            recentAdapterView.notifyRemoved(position)
                        }
                    }
                }
            )
        }
    }

    fun onClickListenerOfKeyword(keyword: String) = view.createResultActivity(keyword)

    override fun loadSearchHistory() {
        searchData.readSearchHistory(
            readSearchHistoryCallback = object : SearchSource.ReadSearchHistoryCallback {
                override fun onReadSearchHistory(list: ArrayList<String>) {
                    suggestedAdapterModel.addItems(list)
                    suggestedAdapterModel.addItemsOnHistoryItemList(list)
                    suggestedAdapterView.notifyAdapter()
                }
            }
        )
    }

    override fun loadResultSearch(keyword: String?) {
        if (!keyword.isNullOrBlank()) {
            productData.readProducts(
                selectedCategory = "total",
                sort = "basic",
                keyword = keyword,
                object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        resultAdapterModel.addItems(list)
                        resultAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            val list = ArrayList<ProductItem>()

            resultAdapterModel.addItems(list)
            resultAdapterView.notifyAdapter()
        }
    }
}