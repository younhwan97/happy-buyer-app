package kr.co.younhwan.happybuyer.view.main.wished.presenter

import android.content.Context
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
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
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

                        for (item in list)
                            if (item.isWished)
                                wishedItem.add(item)

                        adapterModel.addItems(wishedItem)
                        adapterView.notifyAdapter()

                        if (wishedItem.isEmpty())
                            view.setEmpty()

                    }
                }
            )
        } else {
            view.setEmpty()
        }
    }

    private fun onClickListenerOfDeleteBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.addProductToWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.AddProductToWishedCallback{
                    override fun onAddProductToWished(explain: String?) {
                        if (explain.isNullOrBlank()) {

                        } else {
                            adapterModel.deleteItem(position)
                            view.deleteWishedResultCallback()
                        }
                    }
                }
            )
        } else {
            // error
        }
    }

    private fun onClickListenerOfBasketBtn(productId: Int) {

    }
}