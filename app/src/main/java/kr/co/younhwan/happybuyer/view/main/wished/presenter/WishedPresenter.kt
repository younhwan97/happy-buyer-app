package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.main.wished.adapter.contract.WishedAdapterContract

class WishedPresenter(
    val view: WishedContract.View,
    private val productData: ProductRepository,
    private val adapterModel: WishedAdapterContract.Model,
    private val adapterView: WishedAdapterContract.View
) : WishedContract.Presenter {

    init {
        adapterView.onClickFuncOfBasketBtn = { i ->
            onClickListenerOfBasketBtn(i)
        }

        adapterView.onClickFuncOfDeleteBtn = { i, j ->
            onClickListenerOfDeleteBtn(i, j)
        }
    }

    override fun loadWishedItem(context: Context, isClear: Boolean) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) { // 로그인 상태
            productData.readProducts(
                "total",
                sort = "basic",
                object : ProductSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<ProductItem>) {
                        if (isClear) {
                            adapterModel.clearItem()
                        }

                        val wishedProductId = app.wishedProductId
                        val wishedItem = ArrayList<ProductItem>()

                        for(index in 0 until list.size){
                            for(id in wishedProductId){
                                if(id == list[index].productId){
                                    wishedItem.add(list[index])
                                    break;
                                }
                            }
                        }

                        if (wishedItem.isEmpty()) { // 사용자가 찜한 상품이 하나도 없을 때
                            view.setEmpty()
                        } else {
                            adapterModel.addItems(wishedItem)
                            adapterView.notifyAdapter()
                        }
                    }
                }
            )
        } else { // 비 로그인 상태
            view.setEmpty()
        }
    }

    private fun onClickListenerOfDeleteBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.createProductInWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInWishedCallback {
                    override fun onCreateProductInWished(perform: String?) {
                        if (perform == null || perform == "error") {
                            view.deleteWishedResultCallback(perform)
                        } else if (perform == "delete") {
                            for (index in 0 until app.wishedProductId.size) {
                                if (app.wishedProductId[index] == productId) {
                                    app.wishedProductId.removeAt(index)
                                    break
                                }
                            }

                            adapterModel.deleteItem(position)
                            view.deleteWishedResultCallback(perform)
                        }
                    }
                }
            )
        } else {
            view.deleteWishedResultCallback("error")
        }
    }

    private fun onClickListenerOfBasketBtn(productId: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.createProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(success: Boolean) {
                        if (success) {
                            view.addBasketResultCallback(success)
                        } else {

                        }
                    }
                }
            )
        }
    }
}