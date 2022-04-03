package kr.co.younhwan.happybuyer.view.orderdetail

import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.orderproduct.contract.OrderAdapterContract
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource

class OrderDetailPresenter(
    private val view: OrderDetailContract.View,
    private val orderData: OrderRepository,
    private val orderAdapterModel: OrderAdapterContract.Model,
    private val orderAdapterView: OrderAdapterContract.View
) : OrderDetailContract.Model {

    val app = view.getView().application as GlobalApplication

    override fun loadOrderDetail(orderId: Int) {
        if(app.isLogined){
            orderData.readProducts(
                orderId = orderId,
                readProductsCallback = object : OrderSource.ReadProductsCallback{
                    override fun onReadProducts(list: ArrayList<BasketItem>) {
                        view.loadOrderDetailCallback(true)
                        orderAdapterModel.addItems(list)
                        orderAdapterView.notifyAdapter()
                    }
                }
            )
        } else {

        }
    }
}