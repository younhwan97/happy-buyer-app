package kr.co.younhwan.happybuyer.view.category.presenter

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource
import kr.co.younhwan.happybuyer.util.setupBadge
import kr.co.younhwan.happybuyer.view.category.adapter.contract.CategoryAdapterContract

class CategoryPresenter(
    private val view: CategoryContract.View,
    private val productData: ProductRepository,
    private val userData: UserRepository,
    private val adapterModel: CategoryAdapterContract.Model,
    private val adapterView: CategoryAdapterContract.View,
) : CategoryContract.Model {

    init {
        adapterView.onClickFuncOfWishedBtn = { i, j ->
            onClickListenerOfWishedBtn(i, j)
        }

        adapterView.onClickFuncOfBasketBtn = { i, j ->
            onClickListenerOfBasketBtn(i, j)
        }
    }

    override fun loadProducts(isClear: Boolean, selectedCategory: String) {
        val app = ((view.getAct()).application) as GlobalApplication

        productData.readProducts(
            app.kakaoAccountId,
            selectedCategory,
            object : ProductSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        adapterModel.clearItem()

                    adapterModel.addItems(list)
                    adapterView.notifyAdapter()
                }
            })
    }

    private fun onClickListenerOfWishedBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            productData.createProductInWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInWishedCallback {
                    override fun onCreateProductInWished(explain: String?) {
                        if (explain.isNullOrBlank()) {
                            view.createProductInWishedResultCallback("error")
                        } else {
                            adapterView.notifyItemByUsingPayload(position, "wished")
                            view.createProductInWishedResultCallback(explain)
                        }
                    }
                }
            )
        } else {
            view.createLoginActivity()
        }
    }

    private fun onClickListenerOfBasketBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            productData.createProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(isSuccess: Boolean) {
                        if (isSuccess) {
                            if (app.activatedBasket !== "activate") {
                                userData.updateUser(
                                    app.kakaoAccountId!!,
                                    "basket",
                                    "activate",
                                    object : UserSource.UpdateUserCallback {
                                        override fun onUpdateUser(isSuccess: Boolean) {
                                            if(isSuccess) app.activatedBasket = "activate"
                                        }
                                    }
                                )
                            }

                            app.basketItemCount += 1
                            view.getAct().setupBadge(view.getAct().textCartItemCount)
                        }
                        view.createProductInBasketResultCallback(isSuccess)
                    }
                })
        } else {
            view.createLoginActivity()
        }
    }
}