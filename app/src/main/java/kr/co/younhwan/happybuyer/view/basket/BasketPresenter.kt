package kr.co.younhwan.happybuyer.view.basket

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.basket.adapter.contract.BasketAdapterContract

class BasketPresenter(
    private val view: BasketContract.View,
    private val productData: ProductRepository,
    private val basketAdapterModel: BasketAdapterContract.Model,
    private val basketAdapterView: BasketAdapterContract.View
) : BasketContract.Model {

    init {
        basketAdapterView.onClickFuncOfPlusBtn = { i: Int, j: Int ->
            onClickListenerOfPlusBtn(i, j)
        }

        basketAdapterView.onClickFuncOfMinusBtn = { i: Int, j: Int ->
            onClickListenerOfMinusBtn(i, j)
        }

        basketAdapterView.onClickFuncOfDeleteBtn = { i: Int, j: Int ->
            onClickListenerOfDeleteBtn(i, j)
        }
    }

    val app = view.getAct().application as GlobalApplication

    override fun checkAllBasketProduct() {
        if (app.isLogined && basketAdapterModel.getItemCount() != 0) { // 로그인 상태이며 장바구니에 상품이 있을 때
            for(index in 0 until basketAdapterModel.getItemCount()){
                val temp = basketAdapterModel.getItem(index)
                temp.isChecked = !temp.isChecked

                basketAdapterModel.updateItem(position = index, basketItem = temp)
            }

            basketAdapterView.notifyAdapter()
        }
    }

    override fun loadBasketProduct(isClear: Boolean) {
        if (app.isLogined) { // 로그인 상태
            productData.readProductsInBasket(
                app.kakaoAccountId,
                object : ProductSource.ReadProductsInBasketCallback {
                    override fun onReadProductsInBasket(list: ArrayList<BasketItem>) {
                        if (isClear)
                            basketAdapterModel.clearItem()

                        var totalPrice = 0

                        for(index in 0 until list.size){
                            if(list[index].productStatus != "품절"){
                                totalPrice += if(list[index].onSale){
                                    list[index].eventPrice * list[index].countInBasket
                                } else {
                                    list[index].productPrice * list[index].countInBasket
                                }
                            }
                        }

                        view.loadBasketProductCallback(list.size, totalPrice)
                        basketAdapterModel.addItems(list)
                        basketAdapterView.notifyAdapter()
                    }
                }
            )
        } else { // 비로그인 상태
            view.loadBasketProductCallback(0, 0)
            basketAdapterModel.addItems(ArrayList<BasketItem>())
            basketAdapterView.notifyAdapter()
        }
    }


    private fun onClickListenerOfPlusBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.createProductInBasket(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : ProductSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(count: Int) {
                        if (count in 2..19) {
                            basketAdapterView.notifyItemByUsingPayload(position, "plus")
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
                            basketAdapterView.notifyItemByUsingPayload(position, "minus")
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfDeleteBtn(productId: Int, position: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            productData.deleteProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.DeleteProductInBasketCallback {
                    override fun onDeleteProductInBasket(isSuccess: Boolean) {
                        if (isSuccess) {
                            basketAdapterModel.deleteItem(position)
                        }
                    }
                }
            )
        }
    }
}