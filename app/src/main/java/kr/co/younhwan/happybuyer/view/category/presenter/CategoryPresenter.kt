package kr.co.younhwan.happybuyer.view.category.presenter

import android.util.Log
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

    override fun loadProducts(isClear: Boolean, selectedCategory: String, page: Int) {
        val app = view.getAct().application as GlobalApplication

        if (selectedCategory == "행사") {
            eventData.readProducts(
                object : EventSource.ReadProductsCallback {
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
                page = page,
                keyword = null,
                object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear)
                            adapterModel.clearItem()

                        val wishedProductId = app.wishedProductId

                        for (index in 0 until list.size) {
                            for (id in wishedProductId) {
                                if (id == list[index].productId) {
                                    list[index].isWished = true
                                    break
                                }
                            }
                        }

                        view.loadProductsCallback(list.size)
                        adapterModel.addItems(list)
                        adapterView.notifyAdapter()
                    }
                })
        }
    }

    override fun loadMoreProducts(selectedCategory: String, page: Int) {
        val app = view.getAct().application as GlobalApplication

        if (selectedCategory == "행사") {
            eventData.readProducts(
                object : EventSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {

                        view.loadProductsCallback(list.size)
                        adapterModel.addItems(list)
                        adapterView.notifyAdapterByRange((page - 1) * 30, 30)
                    }
                }
            )
        } else {
            productData.readProducts(
                selectedCategory = selectedCategory,
                page = page,
                keyword = null,
                object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        val wishedProductId = app.wishedProductId

                        for (index in 0 until list.size) {
                            for (id in wishedProductId) {
                                if (id == list[index].productId) {
                                    list[index].isWished = true
                                    break
                                }
                            }
                        }

                        view.loadProductsCallback(list.size)
                        adapterModel.addItems(list)
                        adapterView.notifyAdapterByRange((page - 1) * 30, 30)
                    }
                })
        }
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