package kr.co.younhwan.happybuyer.view.basket

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

    val app = view.getAct().application as GlobalApplication

    override fun loadBasketProduct(isClear: Boolean) { // 장바구니에 존재하는 상품을 가져온다.
        if (app.isLogined) { // 로그인 상태
            basketData.readProductsInBasket(
                kakaoAccountId = app.kakaoAccountId,
                readProductsInBasketCallback = object : BasketSource.ReadProductsInBasketCallback {
                    override fun onReadProductsInBasket(list: ArrayList<BasketItem>) {
                        if (isClear)
                            basketAdapterModel.clearItem()

                        basketAdapterModel.addItems(list)
                        basketAdapterView.notifyAdapter()
                        calculatePrice()
                    }
                }
            )
        } else { // 비로그인 상태
            basketAdapterModel.addItems(ArrayList<BasketItem>())
            basketAdapterView.notifyAdapter()
            calculatePrice()
        }
    }

    override fun calculatePrice() {
        var totalPrice = 0 // 결제 예정 금액
        var originalTotalPrice = 0 // 상품을 할인 받기전 원래 금액

        for (index in 0 until basketAdapterModel.getItemCount()) {
            val basketItem = basketAdapterModel.getItem(index)

            if (basketItem.isChecked && basketItem.productStatus != "품절") { // 품절상품은 고려하지 않음
                totalPrice += if (basketItem.onSale) { // 상품이 할인중이라면 할인된 가격을 더한다.
                    basketItem.eventPrice * basketItem.countInBasket
                } else {
                    basketItem.productPrice * basketItem.countInBasket
                }

                originalTotalPrice += basketItem.productPrice * basketItem.countInBasket
            }
        }

        view.calculatePriceCallback(
            totalPrice = totalPrice,
            originalTotalPrice = originalTotalPrice,
            basketItemCount = basketAdapterModel.getItemCount()
        )
    }

    override fun checkAllBasketProduct(newStatus: Boolean) {
        if (app.isLogined && basketAdapterModel.getItemCount() != 0) { // 로그인 상태이며 장바구니에 상품이 있을 때
            for (index in 0 until basketAdapterModel.getItemCount()) {
                if (basketAdapterModel.getItem(index).productStatus != "품절") {
                    val basketItem = basketAdapterModel.getItem(index)
                    basketItem.isChecked = newStatus

                    basketAdapterModel.updateItem(
                        position = index,
                        basketItem = basketItem
                    )
                }
            }

            basketAdapterView.notifyAdapter()
            calculatePrice()
        }
    }


    private fun onClickListenerOfCheckBox(productId: Int, newStatus: Boolean) {
        var isCheckedAllBasketItem = true // 품절 상품을 제외하고 장바구니에 존재하는 모든상품이 체크상태인지

        for (index in 0 until basketAdapterModel.getItemCount()) {
            val basketItem = basketAdapterModel.getItem(index)

            if (basketItem.productId == productId && basketItem.productStatus != "품절") {
                basketItem.isChecked = newStatus
                basketAdapterModel.updateItem(
                    position = index,
                    basketItem = basketItem
                )
                calculatePrice()
            }

            if (!basketItem.isChecked) {
                isCheckedAllBasketItem = false
            }
        }

        view.onClickCheckBoxCallback(isCheckedAllBasketItem)
    }

    private fun onClickListenerOfPlusBtn(basketItem: BasketItem, position: Int) {
        if (app.isLogined) {
            basketData.createProductInBasket(
                kakaoAccountId = app.kakaoAccountId,
                productId = basketItem.productId,
                count = 1,
                createProductInBasketCallback = object :
                    BasketSource.CreateProductInBasketCallback {
                    override fun onCreateProductInBasket(resultCount: Int) {
                        if (resultCount == basketItem.countInBasket + 1) {
                            for (index in 0 until basketAdapterModel.getItemCount()) {
                                if (basketAdapterModel.getItem(index).productId == basketItem.productId) {
                                    basketAdapterModel.updateItemCount(position, resultCount)
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
            basketData.updateProductInBasket(
                kakaoAccountId = app.kakaoAccountId,
                productId = basketItem.productId,
                perform = "minus",
                updateProductInBasketCallback = object :
                    BasketSource.UpdateProductInBasketCallback {
                    override fun onUpdateProductInBasket(isSuccess: Boolean) {
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
            basketData.deleteProductInBasket(
                kakaoAccountId = app.kakaoAccountId,
                productId = basketItem.productId,
                deleteProductInBasketCallback = object :
                    BasketSource.DeleteProductInBasketCallback {
                    override fun onDeleteProductInBasket(isSuccess: Boolean) {
                        if (isSuccess) {
                            basketAdapterModel.deleteItem(position)

                            calculatePrice()
                        }
                    }
                }
            )
        }
    }

    override fun deleteSelectedItems() {
        if (app.isLogined) {
            val newBasketItemList = ArrayList<BasketItem>()
            val selectedItemList = ArrayList<BasketItem>()
            for (index in 0 until basketAdapterModel.getItemCount()) {
                val basketItem = basketAdapterModel.getItem(index)
                if (basketItem.isChecked) {
                    selectedItemList.add(basketItem)
                } else {
                    newBasketItemList.add(basketItem)
                }
            }

            if (selectedItemList.size != 0) {
                MaterialAlertDialogBuilder(view.getAct())
                    .setMessage("선택된 상품을 삭제하시겠습니까?")
                    .setNegativeButton("취소") { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton("확인") { dialog, which ->
                        // basketAdapterModel.addItems(newBasketItemList)
                        // basketAdapterView.notifyAdapter()
                    }
                    .show()
            }
        }
    }
}