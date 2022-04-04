package kr.co.younhwan.happybuyer.view.ordersuccess

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource

class OrderSuccessPresenter(
    private val view: OrderSuccessContract.View,
    private val orderData: OrderRepository
) : OrderSuccessContract.Model {

    override fun checkValidation(order: OrderItem) {
        val app = view.getAct().application as GlobalApplication

        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
                        var passValidationCheck = false

                        for (item in list) {
                            if (item.orderId == order.orderId) {
                                order.date = item.date
                                order.products = item.products
                                if (item == order) {
                                    passValidationCheck = true
                                    break
                                }
                            }
                        }

                        view.checkValidationCallback(passValidationCheck)
                    }
                }
            )
        }
    }
}