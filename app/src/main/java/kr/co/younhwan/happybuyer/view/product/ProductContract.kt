package kr.co.younhwan.happybuyer.view.product

import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomeContract

interface ProductContract {
    interface View {

        fun getAct(): ProductActivity

        fun createLoginActivity()

        fun createProductInWishedResultCallback(productId: Int, perform: String?)

        fun createProductInBasketResultCallback(count:Int)
    }

    interface Model {

        fun clickWishedBtn(productId: Int)

        fun createProductInBasket(kakaoAccountId:Long, productId: Int, count:Int)

    }
}