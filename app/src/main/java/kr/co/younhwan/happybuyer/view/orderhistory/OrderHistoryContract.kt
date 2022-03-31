package kr.co.younhwan.happybuyer.view.orderhistory

interface OrderHistoryContract {
    interface View {

        fun getView(): OrderHistoryActivity

        fun createOrderDetailAct(orderId: Int)
    }

    interface Model {

        fun loadOrderHistory()
    }
}