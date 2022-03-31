package kr.co.younhwan.happybuyer.view.orderdetail

interface OrderDetailContract {
    interface View {
        fun getView(): OrderDetailActivity
    }

    interface Model {
        fun loadOrderDetail(orderId: Int)
    }
}