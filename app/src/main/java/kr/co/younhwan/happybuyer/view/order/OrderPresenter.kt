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
import kr.co.younhwan.happybuyer.adapter.orderproduct.contract.OrderAdapterContract
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.data.source.user.UserSource

class OrderPresenter(
    private val view: OrderContract.View,
    private val addressData: AddressRepository,
    private val userData: UserRepository,
    private val basketData: BasketRepository,
    private val orderData: OrderRepository,
    private val orderAdapterModel: OrderAdapterContract.Model,
    private val orderAdapterView: OrderAdapterContract.View
) : OrderContract.Model {

    private val app = view.getAct().application as GlobalApplication

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

    override fun setOrderProducts(selectedItemList: ArrayList<BasketItem>?) {
        var passValidationCheck = true

        if (app.isLogined && !selectedItemList.isNullOrEmpty()) {
            // ?????????????????? ????????? ????????? ????????? ????????? ??????????????? ???????????? ???
            basketData.readProducts(
                kakaoAccountId = app.kakaoAccountId,
                readProductsCallback = object : BasketSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<BasketItem>) {
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

                        view.setOrderProductsCallback(passValidationCheck)
                        orderAdapterModel.addItems(selectedItemList)
                        orderAdapterView.notifyAdapter()
                        calculatePrice()
                    }
                }
            )
        } else {
            // ?????????????????? ????????? ????????? ????????? ????????? ???????????? ????????? ???
            view.setOrderProductsCallback(!passValidationCheck)
        }
    }

    override fun calculatePrice() {
        var totalPrice = 0 // ?????? ?????? ??????
        var originalTotalPrice = 0 // ????????? ?????? ????????? ?????? ??????

        for (index in 0 until orderAdapterModel.getItemCount()) {
            val orderItem = orderAdapterModel.getItem(index)

            if (orderItem.isChecked && orderItem.productStatus != "??????") {
                totalPrice += if (orderItem.onSale) {
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
                    override fun onCreate(orderId: Int) {
                        if (orderId != -1) {
                            // ????????? ??????????????? ??????????????? ???
                            val orderProductsId = ArrayList<Int>()

                            for (item in orderItem.products) {
                                orderProductsId.add(item.productId)
                            }

                            orderItem.orderId = orderId

                            // ????????? ????????? ????????? ?????????????????? ??????
                            basketData.deleteProducts(
                                kakaoAccountId = app.kakaoAccountId,
                                productId = orderProductsId,
                                deleteProductsCallback = object :
                                    BasketSource.DeleteProductsCallback {
                                    override fun onDeleteProducts(isSuccess: Boolean) {

                                        // ????????? ????????? ????????? ????????? ????????? ????????? ????????????
                                        if ((app.point == null || app.point == "null") && orderItem.point != null && orderItem.point.isNotEmpty()) {
                                            userData.update(
                                                kakaoAccountId = app.kakaoAccountId,
                                                updateTarget = "point",
                                                newContent = orderItem.point,
                                                updateCallback = object :
                                                    UserSource.UpdateCallback {
                                                    override fun onUpdate(isSuccess: Boolean) {
                                                        if (isSuccess) {
                                                            app.point = orderItem.point
                                                        }
                                                        view.createOrderCallback(orderItem)
                                                    }
                                                }
                                            )
                                        } else {
                                            view.createOrderCallback(orderItem)
                                        }
                                    }
                                }
                            )
                        } else {
                            // ????????? ???????????? ???
                            view.createOrderCallback(orderItem)
                        }
                    }
                }
            )
        } else {
            view.createOrderCallback(orderItem)
        }
    }
}