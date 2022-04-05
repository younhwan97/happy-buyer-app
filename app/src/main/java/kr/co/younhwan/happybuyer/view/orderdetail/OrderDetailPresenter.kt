package kr.co.younhwan.happybuyer.view.orderdetail

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.orderproduct.contract.OrderAdapterContract
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource

class OrderDetailPresenter(
    private val view: OrderDetailContract.View,
    private val orderData: OrderRepository,
    private val orderAdapterModel: OrderAdapterContract.Model,
    private val orderAdapterView: OrderAdapterContract.View
) : OrderDetailContract.Model {

    override fun loadOrderProducts(orderId: Int) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            // 로그인 상태일 때
            orderData.readProducts(
                orderId = orderId,
                readProductsCallback = object : OrderSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<BasketItem>) {
                        if (list.size == 0) {
                            // 주문한 상품이 없을 때 (= 에러)
                            view.loadOrderProductsCallback(false)
                        } else {
                            // 주문한 상품이 존재할 때
                            view.loadOrderProductsCallback(true)
                        }
                        orderAdapterModel.addItems(list)
                        orderAdapterView.notifyAdapter()
                    }
                }
            )
        } else {
            // 로그인 상태가 아닐 때
            view.loadOrderProductsCallback(false)
            orderAdapterModel.addItems(ArrayList())
            orderAdapterView.notifyAdapter()
        }
    }
}