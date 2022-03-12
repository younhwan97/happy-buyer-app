package kr.co.younhwan.happybuyer.view.search

import android.util.Log
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
        resultAdapterView.onClickFuncOfProduct = {
            onClickListenerOfProduct(it)
        }

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

    /* Result */
    override fun loadResultSearch(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            // 키워드가 존재하지 않더라도 빈 배열을 추가해야 에러가 발생하지 않는다.
            resultAdapterModel.addItems(ArrayList<ProductItem>())
            resultAdapterView.notifyAdapter()
        } else { // 키워드가 존재할 때
            productData.readProducts(
                selectedCategory = "total",
                sort = "basic",
                keyword = keyword,
                readProductsCallback = object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        view.loadResultSearchCallback(list.size)
                        resultAdapterModel.addItems(list)
                        resultAdapterView.notifyAdapter()
                    }
                }
            )
        }
    }

    private fun onClickListenerOfProduct(productItem: ProductItem) = view.createProductActivity(productItem)

    override fun sortResultSearch(newItem: String) {
        if(resultAdapterModel.getItemCount() > 1){
            val oldList: ArrayList<ProductItem> = resultAdapterModel.getItems()
            val sortedList = when(newItem){
                "추천순" -> {
                    ArrayList(oldList.sortedBy { it.productId })
                }

                "판매순" -> {
                    ArrayList(oldList.sortedBy { it.sales })
                }

                "낮은 가격순" -> {
                    ArrayList(oldList.sortedBy { it.productPrice })
                }

                "높은 가격순" ->{
                    ArrayList(oldList.sortedBy { it.productPrice }.reversed())
                }

                else -> {
                    oldList
                }
            }

            resultAdapterModel.clearItem()
            resultAdapterModel.addItems(sortedList)
            resultAdapterView.notifyAdapter()
        }

        view.sortResultSearchCallback()
    }

    /* Recent */
    override fun createRecentSearch(keyword: String) {
        if (app.isLogined) { // 로그인 상태
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

    /* Suggested */
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
}