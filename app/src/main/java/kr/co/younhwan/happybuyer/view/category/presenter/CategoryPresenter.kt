package kr.co.younhwan.happybuyer.view.category.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.event.EventRepository
import kr.co.younhwan.happybuyer.data.source.event.EventSource
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
    private val eventData: EventRepository,
    private val basketData: BasketRepository,
    private val userData: UserRepository,
    private val adapterModel: ProductAdapterContract.Model,
    private val adapterView: ProductAdapterContract.View,
) : CategoryContract.Model {

    init {
        adapterView.onClickFuncOfBasketBtn = {
            onClickListenerOfBasketBtn(it)
        }

        adapterView.onClickFuncOfProduct = {
            onClickListenerProduct(it)
        }
    }

    val app = view.getAct().application as GlobalApplication

    override fun loadProducts(
        isClear: Boolean,
        selectedCategory: String,
        sortBy: String,
        page: Int
    ) {
        // 초기 상품을 셋팅할 때 호출되는 메서드
        if (selectedCategory == "행사") {
            // 행사 카테고리가 선택된 경우 행사 데이터를 담당하는 모델에 분기
            eventData.readProducts(
                sortBy = sortBy,
                page = page,
                readProductsCallback = object : EventSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear)
                            adapterModel.clearItem()

                        view.loadProductsCallback(list.size)
                        adapterModel.addItems(list)
                        adapterView.notifyAdapter()
                    }
                }
            )
        } else {
            // 행사 카테고리가 아닌 나머지 카테고리의 경우 일반 카테고리를 처리하는 모델에 분기
            productData.readProducts(
                selectedCategory = selectedCategory,
                sortBy = sortBy,
                page = page,
                keyword = null,
                readProductsCallback = object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear)
                            adapterModel.clearItem()

                        view.loadProductsCallback(list.size)
                        adapterModel.addItems(list)
                        adapterView.notifyAdapter()
                    }
                })
        }
    }

    override fun loadMoreProducts(
        selectedCategory: String,
        sortBy: String,
        page: Int
    ) {
        // 상품을 추가로 셋팅할 때 호출되는 메서드
        if (selectedCategory == "행사") {
            // 행사 카테고리가 선택된 경우 행사 데이터를 담당하는 모델에 분기
            eventData.readProducts(
                sortBy = sortBy,
                page = page,
                readProductsCallback = object : EventSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        view.loadProductsCallback(list.size)
                        adapterView.deleteLoading()

                        if (list.size == 0) {
                            // 더 이상 로드할 데이터가 없는 경우 리사이클러 뷰 마지막에 들어가 있는 로딩뷰만 제거
                            adapterView.notifyLastItemRemoved()
                        } else {
                            // 디비로 부터 얻은 데이터를 어댑터에 추가하고 추가된 데이터의 범위 만큼 업데이트
                            adapterModel.addItems(list)
                            adapterView.notifyAdapterByRange(
                                start = adapterModel.getItemCount() - list.size - 1,
                                count = list.size
                            )
                        }
                    }
                }
            )
        } else {
            // 행사 카테고리가 아닌 나머지 카테고리의 경우 일반 카테고리를 처리하는 모델에 분기
            productData.readProducts(
                selectedCategory = selectedCategory,
                sortBy = sortBy,
                page = page,
                keyword = null,
                readProductsCallback = object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        view.loadProductsCallback(list.size)
                        adapterView.deleteLoading()

                        if (list.size == 0) {
                            // 더 이상 로드할 데이터가 없는 경우 리사이클러 뷰 마지막에 들어가 있는 로딩뷰만 제거
                            adapterView.notifyLastItemRemoved()
                        } else {
                            // 디비로 부터 얻은 데이터를 어댑터에 추가하고 추가된 데이터의 범위 만큼 업데이트
                            adapterModel.addItems(list)
                            adapterView.notifyAdapterByRange(
                                start = adapterModel.getItemCount() - list.size - 1,
                                count = list.size
                            )
                        }
                    }
                })
        }
    }

    override fun sortCategoryProducts(newItem: String) {

    }

    private fun onClickListenerProduct(productItem: ProductItem) =
        view.createProductAct(productItem)

    private fun onClickListenerOfBasketBtn(productId: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {

                        if (resultCount in 1..20) {
                            view.createOrUpdateProductInBasketCallback(resultCount)
                        }

                    }
                })
        } else {
            view.createLoginAct()
        }
    }
}