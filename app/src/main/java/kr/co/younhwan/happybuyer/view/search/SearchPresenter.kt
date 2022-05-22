package kr.co.younhwan.happybuyer.view.search

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.RecentItem
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

        resultAdapterView.onClickFuncOfProduct = {
            onClickListenerOfProduct(it)
        }
    }

    private val app = view.getAct().application as GlobalApplication

    fun onClickListenerOfKeyword(keyword: String) {
        view.createResultActivity(keyword)
    }

    private fun onClickListenerOfProduct(productItem: ProductItem) {
        view.createProductActivity(productItem)
    }

    private fun onClickListenerOfDeleteBtn(keyword: String, position: Int) {
        if (app.isLogined) {
            // keyword  == null -> 모든 최근 검색어 삭제
            // keyword !== null -> keyword 에 해당하는 검색어만 삭제
            searchData.deleteRecent(
                kakaoAccountId = app.kakaoAccountId,
                keyword = keyword,
                deleteRecentCallback = object : SearchSource.DeleteRecentCallback {
                    override fun onDeleteRecent(isSuccess: Boolean) {
                        if (isSuccess) {
                            recentAdapterModel.deleteItem(position)
                            recentAdapterView.notifyRemoved(position)
                        }
                    }
                }
            )
        }
    }

    override fun createRecentWithHistory(keyword: String) {
        if (app.isLogined) {
            var alreadyExistsKeyword = false

            for (i in 0 until recentAdapterModel.getItemCount()) {
                if (recentAdapterModel.getItem(i).keyword == keyword) {
                    alreadyExistsKeyword = true
                    break
                }
            }

            if (!alreadyExistsKeyword) {
                searchData.createRecentWithHistory(
                    kakaoAccountId = app.kakaoAccountId,
                    keyword = keyword
                )
            }
        }
    }

    override fun loadRecent() {
        if (app.isLogined) {
            searchData.readRecent(
                kakaoAccountId = app.kakaoAccountId,
                readRecentCallback = object : SearchSource.ReadRecentCallback {
                    override fun onReadRecent(list: ArrayList<RecentItem>) {
                        recentAdapterModel.addItems(list)
                        recentAdapterView.notifyAdapter()
                    }
                }
            )
        }
    }

    override fun loadSearchHistory() {
        searchData.readHistory(
            readHistoryCallback = object : SearchSource.ReadHistoryCallback {
                override fun onReadHistory(list: ArrayList<String>) {
                    suggestedAdapterModel.addItems(list)
                    suggestedAdapterModel.addItemsOnHistoryItemList(list)
                    suggestedAdapterView.notifyAdapter()
                }
            }
        )
    }

    override fun loadSearchResult(isClear: Boolean, keyword: String?, sortBy: String?, page: Int) {
        if (keyword.isNullOrBlank() || keyword.isNullOrEmpty()) {
            // 키워드가 존재하지 않을 때
            view.loadSearchResultCallback(0)
        } else {
            // 키워드가 존재할 때
            productData.readProducts(
                selectedCategory = "전체",
                sortBy = null,
                page = page,
                keyword = keyword,
                readProductsCallback = object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear)
                            resultAdapterModel.clearItem()

                        view.loadSearchResultCallback(list.size)
                        resultAdapterModel.addItems(list)
                        resultAdapterView.notifyAdapter()
                    }
                }
            )
        }
    }

    override fun loadMoreSearchResult(keyword: String?, sortBy: String?, page: Int) {
        if (keyword.isNullOrBlank() || keyword.isNullOrEmpty()) {
            // 키워드가 존재하지 않을 때
            view.loadSearchResultCallback(0)
        } else {
            // 키워드가 존재할 때
            productData.readProducts(
                selectedCategory = "전체",
                sortBy = null,
                page = page,
                keyword = keyword,
                readProductsCallback = object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        view.loadSearchResultCallback(list.size)
                        resultAdapterView.deleteLoading()

                        if (list.size == 0) {
                            // 더 이상 로드할 데이터가 없는 경우 리사이클러 뷰 마지막에 들어가 있는 로딩뷰만 제거
                            resultAdapterView.notifyLastItemRemoved()
                        } else {
                            // 디비로 부터 얻은 데이터를 어댑터에 추가하고 추가된 데이터의 범위 만큼 업데이트
                            resultAdapterModel.addItems(list)
                            resultAdapterView.notifyAdapterByRange(
                                start = resultAdapterModel.getItemCount() - list.size - 1,
                                count = list.size
                            )
                        }
                    }
                }
            )
        }
    }

    override fun deleteAllRecent() {
        if (app.isLogined) {
            if (recentAdapterModel.getItemCount() != 0) { // 저장된 검색어가 있는 경우 (불필요한 api 호출 방지)
                // keyword  == null -> 모든 최근 검색어 삭제
                // keyword !== null -> keyword 에 해당하는 검색어만 삭제
                searchData.deleteRecent(
                    kakaoAccountId = app.kakaoAccountId,
                    keyword = null,
                    deleteRecentCallback = object : SearchSource.DeleteRecentCallback {
                        override fun onDeleteRecent(isSuccess: Boolean) {
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
}