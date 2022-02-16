package kr.co.younhwan.happybuyer.view.category.presenter

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
    private val adapterModel: CategoryAdapterContract.Model,
    private val adapterView: CategoryAdapterContract.View
) : CategoryContract.Model {

    init {
        adapterView.onClickFuncHeartBtn = {
            onClickListenerHeartBtn(it)
        }

        adapterView.onClickFuncShoppingCartBtn = {
            onClickListenerShoppingCartBtn(it)
        }
    }

    override fun loadProductItems(context: Context, isClear: Boolean, selectedCategory: String) {
        productData.getImages(context, selectedCategory ,object : ProductSource.LoadImageCallback {
            override fun onLoadImages(list: ArrayList<ProductItem>) {
                if (isClear) {
                    adapterModel.clearItem()
                }

                adapterModel.addItems(list)
                adapterView.notifyAdapter()
            }
        })
    }

    private fun onClickListenerHeartBtn(productId: Int) {
        productData.addProductToWished(productId)
    }

    private fun onClickListenerShoppingCartBtn(productId: Int) {
        // productData.addProductToBasket(productId)
    }
}