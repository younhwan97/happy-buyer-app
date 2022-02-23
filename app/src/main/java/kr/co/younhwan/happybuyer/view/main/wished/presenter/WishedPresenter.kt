package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
import android.os.SystemClock
import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract
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
            productData.getProducts(
                context,
                "total",
                app.kakaoAccountId!!,
                object : ProductSource.LoadProductCallback {
                    override fun onLoadProducts(list: ArrayList<ProductItem>) {
                        if (isClear) {
                            adapterModel.clearItem()
                        }

                        val wishedItem = ArrayList<ProductItem>()

                        for (item in list) {
                            if (item.isWished) {
                                wishedItem.add(item)
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
            productData.addProductToWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.AddProductToWishedCallback {
                    override fun onAddProductToWished(explain: String?) {
                        if (explain.isNullOrBlank()) {

                        } else {
                            adapterModel.deleteItem(position)
                            view.deleteWishedResultCallback()
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfBasketBtn(productId: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.addProductToBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.AddProductToBasketCallback {
                    override fun onAddProductToBasket(success: Boolean) {
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