package kr.co.younhwan.happybuyer.view.orderdetail

interface OrderDetailContract {
    interface View {

        fun getAct(): OrderDetailActivity

        fun loadOrderProductsCallback(isSuccess: Boolean)

    }

    interface Model {

        fun loadOrderProducts(orderId: Int)

    }
}