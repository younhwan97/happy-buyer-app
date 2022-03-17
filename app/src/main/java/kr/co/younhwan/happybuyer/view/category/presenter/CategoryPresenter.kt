package kr.co.younhwan.happybuyer.view.category.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
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

    override fun loadProducts(isClear: Boolean, selectedCategory: String) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (selectedCategory == "행사") {
            productData.readEventProducts(
                object : ProductSource.ReadEventProductsCallback {
                    override fun onReadEventProduct(list: ArrayList<ProductItem>) {
                        if (isClear)
                            adapterModel.clearItem()

                        adapterModel.addItems(list)
                        adapterView.notifyAdapter()
                    }
                }
            )
        } else {
            productData.readProducts(
                selectedCategory = selectedCategory,
                sort = "basic",
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
                                    break;
                                }
                            }
                        }

                        adapterModel.addItems(list)
                        adapterView.notifyAdapter()
                    }
                })
        }
    }

    private fun onClickListenerProduct(productItem: ProductItem) =
        view.createProductActivity(productItem)

    private fun onClickListenerOfBasketBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            basketData.createProductInBasket(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : BasketSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(count: Int) {

                        if(count in 1..20){
                            view.createProductInBasketResultCallback(count)
                        }

                    }
                })
        } else {
            view.createLoginActivity()
        }
    }
}