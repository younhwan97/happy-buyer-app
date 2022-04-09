package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
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

        popularAdapterView.onClickFuncOfBasketBtn = {i: Int, j: Int ->
            onClickListenerOfBasketBtn(i, j)
        }

        popularAdapterView.onClickFuncOfProduct = {
            onClickListenerOfProduct(it)
        }

    }

    /***********************************************************************/
    /******************************* Category *******************************/
    override fun loadCategories(context: Context, isClear: Boolean) {
        // 프래그먼트가 그려질 때 "초기에" 호출되는 메서드
        // 레파지토리를 통해 카테고리 목록, 이미지, 타이틀을 가져오고 리사이클러뷰를 그린다.
        categoryData.readCategories(context, object : CategorySource.ReadCategoryCallback {
            override fun onReadCategories(list: ArrayList<CategoryItem>) {
                if (isClear)
                    homeAdapterModel.clearItem()

                val mainCategoryList: ArrayList<CategoryItem> =
                    list.take(12) as ArrayList<CategoryItem> // 메인 화면에 노출할 '핵심' 카테고리

                view.setCategoryLabelList(list)
                homeAdapterModel.addItems(mainCategoryList)
                homeAdapterView.notifyAdapter()
            }
        })
    }

    private fun onClickListenerCategoryItem(position: Int) = view.createCategoryActivity(position)


    /***********************************************************************/
    /******************************* Event/Popular *******************************/
    // load event product(data) in event recycler
    override fun loadEventProduct(isClear: Boolean) {
        val app = view.getAct().application as GlobalApplication

        eventData.readProducts(
            object : EventSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        eventAdapterModel.clearItem()

                    val wishedProductId = app.wishedProductId

                    for (index in 0 until list.size) {
                        for (item in wishedProductId) {
                            if (item == list[index].productId) {
                                list[index].isWished = true
                                break;
                            }
                        }
                    }

                    eventAdapterModel.addItems(list)
                    eventAdapterView.notifyAdapter()
                }
            }
        )
    }

    override fun loadPopularProduct(isClear: Boolean) {
        val app = view.getAct().application as GlobalApplication

        productData.readProducts(
            selectedCategory = "total",
            page = 1,
            keyword = null,
            object : ProductSource.ReadProductsCallback {
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        popularAdapterModel.clearItem()

                    val sortedList = ArrayList(list.sortedBy { it.sales }.reversed().subList(0,2))

                    val wishedProductId = app.wishedProductId

                    for (index in 0 until sortedList.size) {
                        for (item in wishedProductId) {
                            if (item == sortedList[index].productId) {
                                sortedList[index].isWished = true
                                break;
                            }
                        }
                    }

                    popularAdapterModel.addItems(sortedList)
                    popularAdapterView.notifyAdapter()
                }
            }
        )
    }

    // set event listener in event recycler
    private fun onClickListenerOfProduct(productItem: ProductItem) = view.createProductActivity(productItem)

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