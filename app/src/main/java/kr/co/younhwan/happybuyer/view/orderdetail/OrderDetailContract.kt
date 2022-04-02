package kr.co.younhwan.happybuyer.view.orderdetail

interface OrderDetailContract {
    interface View {
        fun getView(): OrderDetailActivity

        fun loadOrderDetailCallback(isSuccess: Boolean)
    }

    interface Model {
        fun loadOrderDetail(orderId: Int)
    }
}