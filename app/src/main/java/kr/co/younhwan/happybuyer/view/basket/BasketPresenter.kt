package kr.co.younhwan.happybuyer.view.basket

import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.basket.adapter.contract.BasketAdapterContract

class BasketPresenter(
    private val view: BasketContract.View,
    private val productData: ProductRepository,
    private val adapterModel: BasketAdapterContract.Model,
    private val adapterView: BasketAdapterContract.View
) : BasketContract.Model {

    init {
        adapterView.onClickFuncOfPlusBtn = { i: Int, j: Int ->
            onClickListenerOfPlusBtn(i, j)
        }

        adapterView.onClickFuncOfMinusBtn = { i: Int, j: Int ->
            onClickListenerOfMinusBtn(i, j)
        }

        adapterView.onClickFuncOfDeleteBtn = {i: Int, j: Int ->
            onClickListenerOfDeleteBtn(i, j)
        }
    }

    override fun loadBasketProduct(isClear: Boolean) {
        val app = view.getAct().application as GlobalApplication

        productData.readProductsInBasket(
            app.kakaoAccountId!!,
            object : ProductSource.ReadProductsInBasketCallback {
                override fun onReadProductsInBasket(list: ArrayList<ProductItem>) {
                    if (isClear)
                        adapterModel.clearItem()

                    adapterModel.addItems(list)
                    adapterView.notifyAdapter()
                }
            }
        )
    }


    private fun onClickListenerOfPlusBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.createProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(isSuccess: Boolean) {
                        if (isSuccess) {
                            adapterView.notifyItemByUsingPayload(position, "plus")
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfMinusBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.minusProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.MinusProductInBasketCallback {
                    override fun onMinusProductInBasket(isSuccess: Boolean) {
                        if (isSuccess) {
                            adapterView.notifyItemByUsingPayload(position, "minus")
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfDeleteBtn(productId: Int, position: Int){
        val app = view.getAct().application as GlobalApplication

        if(app.isLogined){
            productData.deleteProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.DeleteProductInBasketCallback{
                    override fun onDeleteProductInBasket(isSuccess: Boolean) {
                        if(isSuccess){
                            adapterModel.deleteItem(position)
                        }
                    }
                }
            )
        }
    }
}