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

    val app = view.getAct().application as GlobalApplication

    // 최근 검색
    override fun createRecentWithHistory(keyword: String) {
        if (app.isLogined) { // 로그인 상태에서만 최근 검색 기록을 생성
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
            // 로그인한 유저의 최근 검색 기록을 읽어온다.
            searchData.readRecent(
                kakaoAccountId = app.kakaoAccountId,
                readRecentCallback = object : SearchSource.ReadRecentCallback {
                    override fun onReadRecent(list: ArrayList<RecentItem>) {
                        recentAdapterModel.addItems(list)
                        recentAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            // 로그인하지 않은 유저
            recentAdapterModel.addItems(ArrayList<RecentItem>())
            recentAdapterView.notifyAdapter()
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

    // 추천 검색
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

    fun onClickListenerOfKeyword(keyword: String) = view.createResultActivity(keyword)

    // 검색 결과
    override fun loadResultSearch(isClear:Boolean, keyword: String?, page: Int) {
        if (keyword.isNullOrBlank() || keyword.isNullOrEmpty()) {
            // 키워드가 존재하지 않을 때
            view.loadSearchResultCallback(0)
            resultAdapterModel.addItems(ArrayList<ProductItem>())
            resultAdapterView.notifyAdapter()
        } else { 
            // 키워드가 존재할 때
            productData.readProducts(
                selectedCategory = "전체",
                sortBy = null,
                page= page,
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

    override fun loadMoreResultSearch(keyword: String?, page: Int) {
        if (keyword.isNullOrBlank() || keyword.isNullOrEmpty()) {
            // 키워드가 존재하지 않을 때
            view.loadSearchResultCallback(0)
            resultAdapterModel.addItems(ArrayList<ProductItem>())
            resultAdapterView.notifyAdapter()
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

    private fun onClickListenerOfProduct(productItem: ProductItem) = view.createProductActivity(productItem)

    override fun sortSearchResult(newItem: String) {
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

        view.sortSearchResultCallback()
    }
}