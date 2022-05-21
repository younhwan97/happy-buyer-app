package kr.co.younhwan.happybuyer.view.main.wished.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedSource
import kr.co.younhwan.happybuyer.view.main.wished.adapter.contract.WishedAdapterContract

class WishedPresenter(
    val view: WishedContract.View,
    private val wishedData: WishedRepository,
    private val basketData: BasketRepository,
    private val wishedAdapterModel: WishedAdapterContract.Model,
    private val wishedAdapterView: WishedAdapterContract.View
) : WishedContract.Presenter {

    init {
        wishedAdapterView.onClickFunOfProduct = {
            onClickListenerOfProduct(it)
        }

        wishedAdapterView.onClickFuncOfBasketBtn = {
            onClickListenerOfBasketBtn(it)
        }

        wishedAdapterView.onClickFuncOfDeleteBtn = { i, j ->
            onClickListenerOfDeleteBtn(i, j)
        }
    }

    private val app = view.getAct().application as GlobalApplication

    private fun onClickListenerOfProduct(productItem: ProductItem) {
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
                }
            )
        }
    }

    private fun onClickListenerOfDeleteBtn(productId: Int, position: Int) {
        if (app.isLogined) {
            wishedData.createOrDeleteProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                createOrDeleteProductCallback = object :
                    WishedSource.CreateOrDeleteProductCallback {
                    override fun onCreateOrDeleteProduct(perform: String?) {
                        if (perform == null || perform == "error") {
                            view.deleteProductInWishedCallback(perform, 0)
                        } else if (perform == "delete") {
                            for (index in 0 until app.wishedProductId.size) {
                                if (app.wishedProductId[index] == productId) {
                                    app.wishedProductId.removeAt(index)
                                    break
                                }
                            }

                            wishedAdapterModel.deleteItem(position)
                            view.deleteProductInWishedCallback(
                                perform,
                                wishedAdapterModel.getItemCount()
                            )
                        }
                    }
                }
            )
        } else {
            view.deleteProductInWishedCallback("error", 0)
        }
    }

    override fun loadWishedProducts(isClear: Boolean) {
        if (app.isLogined) {
            wishedData.readProducts(
                kakaoAccountId = app.kakaoAccountId,
                readProductsCallback = object : WishedSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear)
                            wishedAdapterModel.clearItem()

                        view.loadWishedProductsCallback(list.size)
                        wishedAdapterModel.addItems(list)
                        wishedAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            view.loadWishedProductsCallback(0)
        }
    }
}