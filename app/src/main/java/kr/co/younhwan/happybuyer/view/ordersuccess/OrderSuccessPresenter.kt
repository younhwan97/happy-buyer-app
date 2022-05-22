package kr.co.younhwan.happybuyer.view.ordersuccess

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource

class OrderSuccessPresenter(
    private val view: OrderSuccessContract.View,
    private val orderData: OrderRepository
) : OrderSuccessContract.Model {

    private val app = view.getAct().application as GlobalApplication

    override fun checkValidation(order: OrderItem) {
        var passValidationCheck = false

        if (app.isLogined) {
            orderData.read(
                kakaoAccountId = app.kakaoAccountId,
                pageNum = 1,
                readCallback = object : OrderSource.ReadCallback {
                    override fun onRead(list: ArrayList<OrderItem>) {
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
        } else {
            view.checkValidationCallback(passValidationCheck)
        }
    }
}