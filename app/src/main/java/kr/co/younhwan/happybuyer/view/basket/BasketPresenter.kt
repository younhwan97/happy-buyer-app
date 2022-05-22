package kr.co.younhwan.happybuyer.view.basket

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.view.basket.adapter.contract.BasketAdapterContract

class BasketPresenter(
    private val view: BasketContract.View,
    private val basketData: BasketRepository,
    private val basketAdapterModel: BasketAdapterContract.Model,
    private val basketAdapterView: BasketAdapterContract.View
) : BasketContract.Model {

    init {
        basketAdapterView.onClickFunOfCheckBox = { i: Int, b: Boolean ->
            onClickListenerOfCheckBox(i, b)
        }

        basketAdapterView.onClickFunOfPlusBtn = { basketItem: BasketItem, position: Int ->
            onClickListenerOfPlusBtn(basketItem, position)
        }

        basketAdapterView.onClickFunOfMinusBtn = { basketItem: BasketItem, position: Int ->
            onClickListenerOfMinusBtn(basketItem, position)
        }

        basketAdapterView.onClickFunOfDeleteBtn = { basketItem: BasketItem, position: Int ->
            onClickListenerOfDeleteBtn(basketItem, position)
        }
    }

    private val app = view.getAct().application as GlobalApplication

    private fun onClickListenerOfCheckBox(productId: Int, newStatus: Boolean) {
        var isCheckedAllBasketItem = true // 품절 상품을 제외하고 장바구니에 존재하는 모든상품이 체크상태인지 확인

        for (index in 0 until basketAdapterModel.getItemCount()) {
            if (basketAdapterModel.getItem(index).productId == productId && basketAdapterModel.getItem(
                    index
                ).productStatus != "품절"
            ) {
                basketAdapterModel.updateItemChecked(index, newStatus)
                calculatePrice()
            }

            if (!basketAdapterModel.getItem(index).isChecked) {
                isCheckedAllBasketItem = false
            }
        }

        view.onClickCheckBoxCallback(isCheckedAllBasketItem)
    }

    private fun onClickListenerOfPlusBtn(basketItem: BasketItem, position: Int) {
        if (app.isLogined) {
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = basketItem.productId,
                count = 1,
                createOrUpdateProductCallback = object :
                    BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {
                        if (resultCount == basketItem.countInBasket + 1) {
                            for (index in 0 until basketAdapterModel.getItemCount()) {
                                if (basketAdapterModel.getItem(index).productId == basketItem.productId) {
                                    basketAdapterModel.updateItemCount(
                                        position,
                                        resultCount
                                    )
                                    basketAdapterView.notifyItemByUsingPayload(position, "plus")
                                    break
                                }
                            }

                            calculatePrice()
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfMinusBtn(basketItem: BasketItem, position: Int) {
        if (app.isLogined) {
            basketData.updateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = basketItem.productId,
                perform = "minus",
                updateProductCallback = object :
                    BasketSource.UpdateProductCallback {
                    override fun onUpdateProduct(isSuccess: Boolean) {
                        if (isSuccess) {
                            for (index in 0 until basketAdapterModel.getItemCount()) {
                                if (basketAdapterModel.getItem(index).productId == basketItem.productId) {
                                    basketAdapterModel.updateItemCount(
                                        position,
                                        basketItem.countInBasket - 1
                                    )
                                    basketAdapterView.notifyItemByUsingPayload(position, "minus")
                                    break
                                }
                            }

                            calculatePrice()
                        }
                    }
                }
            )
        }
    }

    private fun onClickListenerOfDeleteBtn(basketItem: BasketItem, position: Int) {
        if (app.isLogined) {
            val selectedItemIdList = ArrayList<Int>()
            selectedItemIdList.add(basketItem.productId)

            if (basketItem.productStatus == "품절") {
                // 품절 상품을 장바구니에서 삭제하고자 할 때
                // -> 다이얼로그를 띄우지 않고 바로 제거
                basketData.deleteProducts(
                    kakaoAccountId = app.kakaoAccountId,
                    productId = selectedItemIdList,
                    deleteProductsCallback = object :
                        BasketSource.DeleteProductsCallback {
                        override fun onDeleteProducts(isSuccess: Boolean) {
                            if (isSuccess) {
                                view.deleteProductInBasketCallback(1)
                                basketAdapterModel.deleteItem(position)

                                calculatePrice()
                            }
                        }
                    }
                )
            } else {
                // 품절 상태가 아닌 상품을 장바구니에서 삭제하고자 할 때
                // -> 다이얼로그를 통해 재확인 후 제거
                MaterialAlertDialogBuilder(view.getAct())
                    .setMessage("상품을 삭제하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { _, _ ->
                        basketData.deleteProducts(
                            kakaoAccountId = app.kakaoAccountId,
                            productId = selectedItemIdList,
                            deleteProductsCallback = object : BasketSource.DeleteProductsCallback {
                                override fun onDeleteProducts(isSuccess: Boolean) {
                                    if (isSuccess) {
                                        view.deleteProductInBasketCallback(1)
                                        basketAdapterModel.deleteItem(position)

                                        calculatePrice()
                                    }
                                }
                            }
                        )
                    }.show()
            }
        }
    }

    override fun loadBasketProducts(isClear: Boolean) {
        if (app.isLogined) {
            basketData.readProducts(
                kakaoAccountId = app.kakaoAccountId,
                readProductsCallback = object : BasketSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<BasketItem>) {
                        if (isClear)
                            basketAdapterModel.clearItem()

                        view.loadBasketProductsCallback(list.size)
                        basketAdapterModel.addItems(list)
                        basketAdapterView.notifyAdapter()
                        calculatePrice()
                    }
                }
            )
        } else {
            view.loadBasketProductsCallback(0)
        }
    }

    override fun checkAllBasketProduct(newStatus: Boolean) {
        if (app.isLogined && basketAdapterModel.getItemCount() != 0) {
            for (index in 0 until basketAdapterModel.getItemCount()) {
                if (basketAdapterModel.getItem(index).productStatus != "품절") {
                    basketAdapterModel.updateItemChecked(index, newStatus)
                }
            }

            basketAdapterView.notifyAdapter()
            calculatePrice()
        }
    }

    override fun deleteSelectedItems() {
        if (app.isLogined) {
            val newBasketItemList = ArrayList<BasketItem>()
            val selectedItemIdList = ArrayList<Int>()

            for (index in 0 until basketAdapterModel.getItemCount()) {
                val basketItem = basketAdapterModel.getItem(index)

                if (basketItem.isChecked) {
                    selectedItemIdList.add(basketItem.productId)
                } else {
                    newBasketItemList.add(basketItem)
                }
            }

            if (selectedItemIdList.size != 0) {
                MaterialAlertDialogBuilder(view.getAct())
                    .setMessage("선택된 상품을 삭제하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { _, _ ->
                        basketData.deleteProducts(
                            kakaoAccountId = app.kakaoAccountId,
                            productId = selectedItemIdList,
                            deleteProductsCallback = object : BasketSource.DeleteProductsCallback {
                                override fun onDeleteProducts(isSuccess: Boolean) {
                                    if (isSuccess) {
                                        view.deleteProductInBasketCallback(selectedItemIdList.size)
                                        basketAdapterModel.addItems(newBasketItemList)
                                        basketAdapterView.notifyAdapter()
                                        calculatePrice()
                                    }
                                }
                            }
                        )
                    }.show()
            }
        }
    }

    override fun calculatePrice() {
        var totalPrice = 0 // 결제 예정 금액
        var originalTotalPrice = 0 // 상품을 할인 받기전 원래 금액

        for (index in 0 until basketAdapterModel.getItemCount()) {
            val basketItem = basketAdapterModel.getItem(index)

            if (basketItem.isChecked && basketItem.productStatus != "품절") {
                originalTotalPrice += basketItem.productPrice * basketItem.countInBasket

                totalPrice += if (basketItem.onSale) {
                    basketItem.eventPrice * basketItem.countInBasket
                } else {
                    basketItem.productPrice * basketItem.countInBasket
                }
            }
        }

        view.calculatePriceCallback(
            totalPrice = totalPrice,
            originalTotalPrice = originalTotalPrice,
            basketItemCount = basketAdapterModel.getItemCount()
        )
    }

    override fun createOrderAct() {
        if (app.isLogined) {
            basketData.readProducts(
                kakaoAccountId = app.kakaoAccountId,
                readProductsCallback = object : BasketSource.ReadProductsCallback {
                    override fun onReadProducts(list: ArrayList<BasketItem>) {
                        var passValidationCheck = true
                        val selectedBasketItem = ArrayList<BasketItem>()

                        for (index in 0 until basketAdapterModel.getItemCount()) {
                            for (item in list) {
                                if (basketAdapterModel.getItem(index).productId == item.productId) {
                                    item.isChecked = basketAdapterModel.getItem(index).isChecked

                                    if (basketAdapterModel.getItem(index) != item) {
                                        passValidationCheck = false
                                        break
                                    }
                                }
                            }

                            if (!passValidationCheck) break

                            if (basketAdapterModel.getItem(index).isChecked)
                                selectedBasketItem.add(basketAdapterModel.getItem(index))
                        }

                        view.createOrderActCallback(passValidationCheck, selectedBasketItem)
                    }
                }
            )
        } else {
            view.createOrderActCallback(false, ArrayList())
        }
    }
}