package kr.co.younhwan.happybuyer.view.order

import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.data.source.address.AddressSource
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.data.source.order.OrderSource
import kr.co.younhwan.happybuyer.view.order.adapter.contract.OrderAdapterContract

class OrderPresenter(
    private val view: OrderContract.View,
    private val addressData: AddressRepository,
    private val basketData: BasketRepository,
    private val orderData: OrderRepository,
    private val orderAdapterModel: OrderAdapterContract.Model,
    private val orderAdapterView: OrderAdapterContract.View
) : OrderContract.Model {

    val app = view.getAct().application as GlobalApplication

    override fun loadDefaultAddress() {
        if (app.isLogined) {
            addressData.read(
                kakaoAccountId = app.kakaoAccountId,
                readCallback = object : AddressSource.ReadCallback {
                    override fun onRead(list: ArrayList<AddressItem>) {
                        var defaultAddressItem: AddressItem? = null

                        for (item in list) {
                            if (item.isDefault == true) {
                                defaultAddressItem = item
                                break
                            }
                        }

                        view.loadDefaultAddressCallback(defaultAddressItem)
                    }
                }
            )
        }
    }

    override fun setOrderProduct(selectedItemList: ArrayList<BasketItem>?) {
        if (app.isLogined) {
            if (!selectedItemList.isNullOrEmpty()) {
                basketData.readProducts(
                    kakaoAccountId = app.kakaoAccountId,
                    readProductsCallback = object : BasketSource.ReadProductsCallback {
                        override fun onReadProducts(list: ArrayList<BasketItem>) {
                            var passValidationCheck = true

                            for (index in 0 until list.size) {
                                for (item in selectedItemList) {
                                    if (list[index].productId == item.productId) {
                                        if (list[index] != item) {
                                            passValidationCheck = false
                                            break
                                        }
                                    }
                                }

                                if (!passValidationCheck) break
                            }

                            if (passValidationCheck) {
                                orderAdapterModel.addItems(selectedItemList)
                                orderAdapterView.notifyAdapter()
                                calculatePrice()
                            } else {
                                view.setOrderProductCallback(false)
                            }
                        }
                    }
                )
            } else {
                view.setOrderProductCallback(false)
            }
        }
    }

    override fun calculatePrice() {
        var totalPrice = 0 // 결제 예정 금액
        var originalTotalPrice = 0 // 상품을 할인 받기전 원래 금액

        for (index in 0 until orderAdapterModel.getItemCount()) {
            val orderItem = orderAdapterModel.getItem(index)

            if (orderItem.isChecked && orderItem.productStatus != "품절") { // 품절상품은 고려하지 않음
                totalPrice += if (orderItem.onSale) { // 상품이 할인중이라면 할인된 가격을 더한다.
                    orderItem.eventPrice * orderItem.countInBasket
                } else {
                    orderItem.productPrice * orderItem.countInBasket
                }

                originalTotalPrice += orderItem.productPrice * orderItem.countInBasket
            }
        }

        view.calculatePriceCallback(
            totalPrice = totalPrice,
            originalTotalPrice = originalTotalPrice,
            basketItemCount = orderAdapterModel.getItemCount()
        )
    }

    override fun createOrder(orderItem: OrderItem) {
        if (app.isLogined) {
            orderData.create(
                kakaoAccountId = app.kakaoAccountId,
                orderItem = orderItem,
                createCallback = object : OrderSource.CreateCallback {
                    override fun onCreate(isSuccess: Boolean) {
                        if (isSuccess) { // 주문이 성공적으로 완료되었을 때
                            val orderProductsId = ArrayList<Int>()

                            for (item in orderItem.products) {
                                orderProductsId.add(item.productId)
                            }

                            // 주문이 완료된 상품을 장바구니에서 제거
                            basketData.deleteProducts(
                                kakaoAccountId = app.kakaoAccountId,
                                productId = orderProductsId,
                                deleteProductsCallback = object :
                                    BasketSource.DeleteProductsCallback {
                                    override fun onDeleteProducts(isSuccess: Boolean) {
                                        view.createOrderCallback(isSuccess)
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}