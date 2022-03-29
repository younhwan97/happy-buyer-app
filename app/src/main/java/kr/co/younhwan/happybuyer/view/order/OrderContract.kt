package kr.co.younhwan.happybuyer.view.order

import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem

interface OrderContract {
    interface View {

        fun getAct(): OrderActivity

        fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?)

        fun setOrderProductCallback(isSuccess: Boolean)

        fun calculatePriceCallback(totalPrice: Int, originalTotalPrice: Int, basketItemCount: Int)

        fun createOrderCallback(orderId: Int)
    }

    interface Model {

        fun loadDefaultAddress()

        fun setOrderProduct(selectedItemList: ArrayList<BasketItem>?)

        fun calculatePrice()

        fun createOrder(orderItem: OrderItem)
    }
}