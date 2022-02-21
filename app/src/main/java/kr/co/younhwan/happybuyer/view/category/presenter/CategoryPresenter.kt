package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import android.util.Log
import android.view.View
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
    private val adapterModel: CategoryAdapterContract.Model,
    private val adapterView: CategoryAdapterContract.View,
) : CategoryContract.Model {

    init {
        adapterView.onClickFuncOfWishedBtn = { i, j ->
            onClickListenerOfWishedBtn(i, j)
        }

        adapterView.onClickFuncOfBasketBtn = {
            onClickListenerOfBasketBtn(it)
        }
    }

    override fun loadProductItems(context: Context, isClear: Boolean, selectedCategory: String) {
        val act = view.getAct()
        val app = act.application as GlobalApplication

        productData.getProducts(
            context,
            selectedCategory,
            app.kakaoAccountId,
            object : ProductSource.LoadProductCallback {
                override fun onLoadProducts(list: ArrayList<ProductItem>) {
                    if (isClear) {
                        adapterModel.clearItem()
                    }

                    adapterModel.addItems(list)
                    adapterView.notifyAdapter()
                }
            })
    }

    private fun onClickListenerOfWishedBtn(productId: Int, position: Int) {

        val app = view.getAct().application as GlobalApplication

        productData.addProductToWished(
            app.kakaoAccountId!!,
            productId,
            object : ProductSource.AddProductToWishedCallback {
                override fun onAddProductToWished(explain: String?) {
                    if (explain.isNullOrBlank()) {

                    } else {
                        adapterModel.updateProduct(position, "wished")
                        adapterView.notifyItem(position)
                        view.addWishedResultCallback(explain)
                    }
                }
            }
        )
    }

    private fun onClickListenerOfBasketBtn(productId: Int) {

        val app = view.getAct().application as GlobalApplication

        productData.addProductToBasket(
            app.kakaoAccountId!!,
            productId,
            object : ProductSource.AddProductCallback {
                override fun onAddProduct(success: Boolean) {
                    if (success) {

                    } else {

                    }
                }
            })
    }
}