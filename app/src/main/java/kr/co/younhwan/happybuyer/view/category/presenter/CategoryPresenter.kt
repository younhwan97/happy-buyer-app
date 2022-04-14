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
        adapterView.onClickFuncOfBasketBtn = { i, j ->
            onClickListenerOfBasketBtn(i, j)
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
        if (selectedCategory == "행사") {
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
        if (selectedCategory == "행사") {
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
                            adapterModel.addItems(list)
                            adapterView.notifyAdapterByRange(
                                adapterModel.getItemCount() - list.size - 1,
                                list.size
                            )
                        }
                    }
                }
            )
        } else {
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
                            adapterModel.addItems(list)
                            adapterView.notifyAdapterByRange(
                                adapterModel.getItemCount() - list.size - 1,
                                list.size
                            )
                        }
                    }
                })
        }
    }

    override fun sortCategoryProducts(newItem: String) {

    }

    private fun onClickListenerProduct(productItem: ProductItem) =
        view.createProductActivity(productItem)

    private fun onClickListenerOfBasketBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {

                        if (resultCount in 1..20) {
                            view.createProductInBasketResultCallback(resultCount)
                        }

                    }
                })
        } else {
            view.createLoginActivity()
        }
    }
}