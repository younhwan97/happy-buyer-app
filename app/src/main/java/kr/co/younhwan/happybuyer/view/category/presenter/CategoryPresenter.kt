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
            selectedCategory = selectedCategory,
            sort = "basic",
            object : ProductSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        adapterModel.clearItem()

                    val wishedProductId = app.wishedProductId

                    for(index in 0 until list.size){
                        for(id in wishedProductId){
                            if(id == list[index].productId){
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

    private fun onClickListenerOfWishedBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            productData.createProductInWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInWishedCallback {
                    override fun onCreateProductInWished(perform: String?) {
                        if (perform == "error" || perform == null) {
                            view.createProductInWishedResultCallback(perform)
                        } else if (perform == "delete" || perform == "create"){

                            if(perform == "create"){
                                var isMatched = false

                                for(id in app.wishedProductId){
                                    if(id == productId){
                                        isMatched = true
                                        break
                                    }
                                }

                                if(!isMatched) app.wishedProductId.add(productId)

                            } else if(perform == "delete"){
                                for(index in 0 until app.wishedProductId.size){
                                    if(app.wishedProductId[index] == productId){
                                        app.wishedProductId.removeAt(index)
                                        break
                                    }
                                }
                            }

                            adapterView.notifyItemByUsingPayload(position, "wished")
                            view.createProductInWishedResultCallback(perform)
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfBasketBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (app.isLogined) {
            productData.createProductInBasket(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(count: Int) {

//                        if (app.activatedBasket !== "activate") {
//                            userData.updateUser(
//                                app.kakaoAccountId!!,
//                                "basket",
//                                "activate",
//                                object : UserSource.UpdateUserCallback {
//                                    override fun onUpdateUser(isSuccess: Boolean) {
//                                        if(isSuccess) app.activatedBasket = "activate"
//                                    }
//                                }
//                            )
//                        }
//
//                        app.basketItemCount += 1
//                        view.getAct().setupBadge(view.getAct().textCartItemCount)

                        view.createProductInBasketResultCallback(count)
                    }
                })
        } else {
            view.createLoginActivity()
        }
    }
}