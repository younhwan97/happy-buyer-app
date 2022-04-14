package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.adapter.product.contract.ProductAdapterContract
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.basket.BasketSource
import kr.co.younhwan.happybuyer.data.source.category.CategoryRepository
import kr.co.younhwan.happybuyer.data.source.category.CategorySource
import kr.co.younhwan.happybuyer.data.source.event.EventRepository
import kr.co.younhwan.happybuyer.data.source.event.EventSource
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.main.home.adapter.main.contract.HomeAdapterContract

class HomePresenter(
    private val view: HomeContract.View,
    private val categoryData: CategoryRepository,
    private val productData: ProductRepository,
    private val eventData: EventRepository,
    private val basketData: BasketRepository,
    private val homeAdapterModel: HomeAdapterContract.Model,
    private val homeAdapterView: HomeAdapterContract.View,
    private val eventAdapterModel: ProductAdapterContract.Model,
    private val eventAdapterView: ProductAdapterContract.View,
    private val popularAdapterModel: ProductAdapterContract.Model,
    private val popularAdapterView: ProductAdapterContract.View,
) : HomeContract.Presenter {

    init {
        homeAdapterView.onClickFuncOfCategoryItem = { i: Int ->
            onClickListenerCategoryItem(i)
        }

        eventAdapterView.onClickFuncOfBasketBtn = { i: Int, j: Int ->
            onClickListenerOfBasketBtn(i, j)
        }

        eventAdapterView.onClickFuncOfProduct = {
            onClickListenerOfProduct(it)
        }

        popularAdapterView.onClickFuncOfBasketBtn = { i: Int, j: Int ->
            onClickListenerOfBasketBtn(i, j)
        }

        popularAdapterView.onClickFuncOfProduct = {
            onClickListenerOfProduct(it)
        }

    }

    val app = view.getAct().application as GlobalApplication

    override fun loadCategories(isClear: Boolean, context: Context) {
        categoryData.readCategories(
            context = context,
            loadImageCallback = object : CategorySource.ReadCategoryCallback {
                override fun onReadCategories(list: ArrayList<CategoryItem>) {
                    if (isClear)
                        homeAdapterModel.clearItem()

                    val mainCategoryList: ArrayList<CategoryItem> =
                        list.take(12) as ArrayList<CategoryItem> // 메인 화면에 노출할 '핵심' 카테고리

                    view.loadCategoriesCallback(list)
                    homeAdapterModel.addItems(mainCategoryList)
                    homeAdapterView.notifyAdapter()
                }
            })
    }

    private fun onClickListenerCategoryItem(position: Int) = view.createCategoryActivity(position)

    override fun loadEventProduct(isClear: Boolean) {
        eventData.readProducts(
            sortBy = "판매순",
            page = 1,
            object : EventSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        eventAdapterModel.clearItem()

                    eventAdapterModel.addItems(list)
                    eventAdapterView.deleteLoading()
                    eventAdapterView.notifyAdapter()
                }
            }
        )
    }

    override fun loadPopularProduct(isClear: Boolean) {
        productData.readProducts(
            selectedCategory = "전체",
            sortBy = "판매순",
            page = 1,
            keyword = null,
            object : ProductSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        popularAdapterModel.clearItem()

                    val sortedList = ArrayList(list.sortedBy { it.sales }.reversed())

                    popularAdapterModel.addItems(sortedList)
                    popularAdapterView.deleteLoading()
                    popularAdapterView.notifyAdapter()
                }
            }
        )
    }
    
    private fun onClickListenerOfProduct(productItem: ProductItem) =
        view.createProductActivity(productItem)

    private fun onClickListenerOfBasketBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            basketData.createOrUpdateProduct(
                kakaoAccountId = app.kakaoAccountId,
                productId = productId,
                count = 1,
                object : BasketSource.CreateOrUpdateProductCallback {
                    override fun onCreateOrUpdateProduct(resultCount: Int) {
                        if (resultCount in 1..20) {

                            view.createProductInBasketResultCallback(resultCount)
                        }
                    }
                }
            )
        }
    }
}