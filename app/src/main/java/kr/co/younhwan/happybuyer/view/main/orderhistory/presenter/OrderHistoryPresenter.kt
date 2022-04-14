package kr.co.younhwan.happybuyer.view.main.orderhistory.presenter

import android.util.Log
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

    val app = view.getAct().application as GlobalApplication

    private fun onClickListener(orderHistoryItem: OrderItem) =
        view.createOrderDetailAct(orderHistoryItem)

    override fun loadOrderHistory(isClear: Boolean, page: Int) {
        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                pageNum = page,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
                        if (isClear) {
                            orderHistoryAdapterModel.clearItem()
                        }

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
                            // 더 이상 로드할 데이터가 없는 경우 리사이클러 뷰 마지막에 들어가 있는 로딩뷰만 제거
                            orderHistoryAdapterView.notifyLastItemRemoved()
                        } else {
                            // 디비로 부터 얻은 데이터를 어댑터에 추가하고 추가된 데이터의 범위 만큼 업데이트
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
            orderHistoryAdapterModel.addItems(ArrayList())
            orderHistoryAdapterView.notifyAdapter()
        }
    }
}