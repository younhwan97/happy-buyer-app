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

    private val app = view.getAct().application as GlobalApplication

    private fun onClickListenerProduct(productItem: ProductItem) {
        view.createProductAct(productItem)
    }

    private fun onClickListenerOfBasketBtn(productId: Int) {
        if (app.isLogined) {
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {
                        view.createOrUpdateProductInBasketCallback(resultCount)
                    }
                })
        } else {
            view.createLoginAct()
        }
    }

    override fun loadProducts(
        isClear: Boolean,
        selectedCategory: String,
        sortBy: String,
        page: Int
    ) {
        if (selectedCategory == "??????") {
            // ?????? ??????????????? ????????? ??????
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
            // ?????? ??????????????? ?????? ????????? ??????????????? ??????
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
        if (selectedCategory == "??????") {
            // ?????? ??????????????? ????????? ??????
            eventData.readProducts(
                sortBy = sortBy,
                page = page,
                readProductsCallback = object : EventSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        view.loadProductsCallback(list.size)
                        adapterView.deleteLoading()

                        if (list.size == 0) {
                            // ??? ?????? ????????? ???????????? ?????? ?????? ??????????????? ??? ???????????? ????????? ?????? ???????????? ??????
                            adapterView.notifyLastItemRemoved()
                        } else {
                            // ????????? ?????? ?????? ???????????? ???????????? ???????????? ????????? ???????????? ?????? ?????? ????????????
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
            // ?????? ??????????????? ?????? ????????? ??????????????? ??????
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
                            // ??? ?????? ????????? ???????????? ?????? ?????? ??????????????? ??? ???????????? ????????? ?????? ???????????? ??????
                            adapterView.notifyLastItemRemoved()
                        } else {
                            // ????????? ?????? ?????? ???????????? ???????????? ???????????? ????????? ???????????? ?????? ?????? ????????????
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
}