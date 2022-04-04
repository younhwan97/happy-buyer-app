package kr.co.younhwan.happybuyer.view.ordersuccess

import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderSuccessContract {
    interface View {

        fun getAct(): OrderSuccessActivity

        fun checkValidationCallback(isSuccess: Boolean)

    }

    interface Model {

        fun checkValidation(order: OrderItem)

    }
}