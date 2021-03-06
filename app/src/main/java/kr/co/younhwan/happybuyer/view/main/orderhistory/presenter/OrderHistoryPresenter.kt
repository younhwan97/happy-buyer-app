package kr.co.younhwan.happybuyer.view.main.orderhistory.presenter

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

    private val app = view.getAct().application as GlobalApplication

    private fun onClickListener(orderHistoryItem: OrderItem) {
        view.createOrderDetailAct(orderHistoryItem)
    }

    override fun loadOrderHistory(isClear: Boolean, page: Int) {
        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                pageNum = page,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
                        if (isClear)
                            orderHistoryAdapterModel.clearItem()

                        view.loadOrderHistoryCallback(list.size)
                        orderHistoryAdapterModel.addItems(list)
                        orderHistoryAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            view.loadOrderHistoryCallback(0)
        }
    }

    override fun loadMoreOrderHistory(page: Int) {
        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                pageNum = page,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
                        view.loadOrderHistoryCallback(list.size)
                        orderHistoryAdapterView.deleteLoading()

                        if (list.size == 0) {
                            // ??? ?????? ????????? ???????????? ?????? ??????
                            // ??????????????? ??? ???????????? ????????? ?????? ???????????? ??????
                            orderHistoryAdapterView.notifyLastItemRemoved()
                        } else {
                            // ????????? ?????? ?????? ???????????? ???????????? ???????????? ????????? ???????????? ?????? ?????? ????????????
                            orderHistoryAdapterModel.addItems(list)
                            orderHistoryAdapterView.notifyAdapterByRange(
                                start = orderHistoryAdapterModel.getItemCount() - list.size - 1,
                                count = list.size
                            )
                        }
                    }
                }
            )
        } else {
            view.loadOrderHistoryCallback(0)
        }
    }
}