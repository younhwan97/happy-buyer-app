package kr.co.younhwan.happybuyer.view.product

import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomeContract

interface ProductContract {
    interface View {

        fun getAct(): ProductActivity

        fun createLoginActivity()

        fun createOrDeleteProductInWishedCallback(productId: Int, perform: String?)

        fun createOrUpdateProductInBasketCallback(resultCount: Int, basketItemCount: Int)

    }

    interface Model {

        fun clickWishedBtn(productId: Int)

        fun createOrUpdateProductInBasket(productId: Int, count: Int)

    }
}