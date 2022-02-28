package kr.co.younhwan.happybuyer.view.main.home.presenter

import android.content.Context
import android.util.Log
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.data.CategoryItem
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.category.CategoryRepository
import kr.co.younhwan.happybuyer.data.source.category.CategorySource
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductSource
import kr.co.younhwan.happybuyer.view.main.home.adapter.event.contract.EventAdapterContract
import kr.co.younhwan.happybuyer.view.main.home.adapter.main.contract.HomeAdapterContract
import kr.co.younhwan.happybuyer.view.main.home.adapter.popular.contract.PopularAdapterContract

class HomePresenter(
    private val view: HomeContract.View,
    private val categoryData: CategoryRepository,
    private val productData: ProductRepository,
    private val homeAdapterModel: HomeAdapterContract.Model,
    private val homeAdapterView: HomeAdapterContract.View,
    private val eventAdapterModel: EventAdapterContract.Model,
    private val eventAdapterView: EventAdapterContract.View,
    private val popularAdapterModel: PopularAdapterContract.Model,
    private val popularAdapterView: PopularAdapterContract.View
) : HomeContract.Presenter {

    init {
        homeAdapterView.onClickFuncOfCategoryItem= { i: Int ->
            onClickListenerCategoryItem(i)
        }

        eventAdapterView.onClickFuncOfWishedBtn = { i: Int, j: Int ->
            onClickListenerOfEventWishedBtn(i, j)
        }

        popularAdapterView.onClickFuncOfWishedBtn = { i: Int, j: Int ->
            onClickListenerOfPopularWishedBtn(i, j)
        }
    }

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

    override fun loadEventProduct(isClear: Boolean) {
        val app = view.getAct().application as GlobalApplication

        productData.readEventProducts(
            object : ProductSource.ReadEventProductsCallback {
                override fun onReadEventProduct(list: ArrayList<ProductItem>) {
                    if (isClear)
                        eventAdapterModel.clearItem()

                    val wishedProductId = app.wishedProductId

                    for(index in 0 until list.size){
                        for(item in wishedProductId){
                            if(item == list[index].productId){
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

    private fun onClickListenerOfEventWishedBtn(productId: Int, position: Int) {
        val app = ((view.getAct()).application) as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            productData.createProductInWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInWishedCallback {
                    override fun onCreateProductInWished(perform: String?) {
                        if (perform == null || perform == "error") {
                            view.createProductInWishedResultCallback(perform)
                        } else if(perform == "delete" || perform == "create"){

                            if(perform == "create"){
                                var isMatched = false

                                for(id in app.wishedProductId){
                                    if(id == productId){
                                        isMatched = true
                                        break
                                    }
                                }

                                if(!isMatched) app.wishedProductId.add(productId)

                            } else if(perform == "delete"){
                                for(index in 0 until app.wishedProductId.size){
                                    if(app.wishedProductId[index] == productId){
                                        app.wishedProductId.removeAt(index)
                                        break
                                    }
                                }
                            }

                            for(index in 0 until popularAdapterModel.getItemCount()){
                                if(popularAdapterModel.getItem(index).productId == productId){
                                    popularAdapterView.notifyItemByUsingPayload(index, "wished")
                                    break
                                }
                            }

                            eventAdapterView.notifyItemByUsingPayload(position, "wished")
                            view.createProductInWishedResultCallback(perform)
                        }
                    }
                }
            )
        }
    }

    override fun loadPopularProduct(isClear: Boolean) {
        val app = view.getAct().application as GlobalApplication

        productData.readProducts(
            "total",
            "popular",
            object : ProductSource.ReadProductsCallback{
                override fun onReadProducts(list: ArrayList<ProductItem>) {
                    if (isClear)
                        popularAdapterModel.clearItem()

                    val wishedProductId = app.wishedProductId

                    for(index in 0 until list.size){
                        for(item in wishedProductId){
                            if(item == list[index].productId){
                                list[index].isWished = true
                                break;
                            }
                        }
                    }

                    popularAdapterModel.addItems(list)
                    popularAdapterView.notifyAdapter()
                }
            }
        )
    }

    private fun onClickListenerOfPopularWishedBtn(productId: Int, position: Int){
        val app = ((view.getAct()).application) as GlobalApplication

        if (!app.isLogined) {
            view.createLoginActivity()
        } else {
            productData.createProductInWished(
                app.kakaoAccountId!!,
                productId,
                object : ProductSource.CreateProductInWishedCallback{
                    override fun onCreateProductInWished(perform: String?) {
                        if(perform == null || perform == "error"){
                            view.createProductInWishedResultCallback(perform)
                        }else if(perform == "create" || perform == "delete"){

                            if(perform == "create"){
                                var isMatched = false

                                for(id in app.wishedProductId){
                                    if(id == productId){
                                        isMatched = true
                                        break
                                    }
                                }

                                if(!isMatched) app.wishedProductId.add(productId)

                            } else if(perform == "delete"){
                                for(index in 0 until app.wishedProductId.size){
                                    if(app.wishedProductId[index] == productId){
                                        app.wishedProductId.removeAt(index)
                                        break
                                    }
                                }
                            }

                            for(index in 0 until eventAdapterModel.getItemCount()){
                                if(eventAdapterModel.getItem(index).productId == productId){
                                    eventAdapterView.notifyItemByUsingPayload(index, "wished")
                                    break
                                }
                            }

                            popularAdapterView.notifyItemByUsingPayload(position, "wished")
                            view.createProductInWishedResultCallback(perform)
                        }
                    }
                }
            )
        }
    }

    override fun dataRefresh() {
        val app = view.getAct().application as GlobalApplication
        val wishedProductId = app.wishedProductId

        /* refresh data of event product recycler */
        if(eventAdapterModel.getItemCount() != 0){
            for(index in 0 until eventAdapterModel.getItemCount()){
                var isMatched = false

                for(id in wishedProductId){
                    if(id == eventAdapterModel.getItem(index).productId){
                        isMatched = true
                        break;
                    }
                }

                val newProduct = eventAdapterModel.getItem(index)
                newProduct.isWished = isMatched
                eventAdapterModel.updateProduct(index, newProduct)
            }

            eventAdapterView.notifyAdapter()
        }

        /* refresh data of popular product recycler */
        if(popularAdapterModel.getItemCount() != 0){
            for(index in 0 until popularAdapterModel.getItemCount()){
                var isMatched = false

                for(id in wishedProductId){
                    if(id == popularAdapterModel.getItem(index).productId){
                        isMatched = true
                        break;
                    }
                }

                val newProduct = popularAdapterModel.getItem(index)
                newProduct.isWished = isMatched
                popularAdapterModel.updateProduct(index, newProduct)
            }

            popularAdapterView.notifyAdapter()
        }
    }
}