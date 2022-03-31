package kr.co.younhwan.happybuyer.view.orderhistory

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource
import kr.co.younhwan.happybuyer.view.orderhistory.adapter.contract.OrderHistoryAdapterContract

class OrderHistoryPresenter(
    private val view: OrderHistoryContract.View,
    private val orderData: OrderRepository,
    private val orderHistoryAdapterModel: OrderHistoryAdapterContract.Model,
    private val orderHistoryAdapterView: OrderHistoryAdapterContract.View
) : OrderHistoryContract.Model {

    init {
        orderHistoryAdapterView.onClickFun = {
            onClickListener(it)
        }
    }

    val app = view.getView().application as GlobalApplication

    private fun onClickListener(orderHistoryItem: OrderItem) =
        view.createOrderDetailAct(orderHistoryItem)

    override fun loadOrderHistory() {
        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
                        view.loadOrderHistoryCallback(list.size)
                        orderHistoryAdapterModel.addItems(list)
                        orderHistoryAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            view.loadOrderHistoryCallback(0)
            orderHistoryAdapterModel.addItems(ArrayList())
            orderHistoryAdapterView.notifyAdapter()
        }
    }
}