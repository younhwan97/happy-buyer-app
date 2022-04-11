package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.event.EventRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryContract
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryPresenter
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.view.product.ProductActivity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class CategoryFragment : Fragment(), CategoryContract.View {
    private lateinit var viewDataBinding: FragmentCategoryBinding

    private val categoryPresenter: CategoryPresenter by lazy {
        CategoryPresenter(
            this,
            productData = ProductRepository,
            eventData = EventRepository,
            basketData = BasketRepository,
            userData = UserRepository,
            adapterModel = productAdapter,
            adapterView = productAdapter
        )
    }

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter("category")
    }

    private var nowPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentCategoryBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로딩 뷰 셋팅
        viewDataBinding.categoryView.visibility = View.GONE
        viewDataBinding.categoryEmptyView.visibility = View.GONE
        viewDataBinding.categoryLoadingView.visibility = View.VISIBLE
        viewDataBinding.categoryLoadingImage.playAnimation()

        // 인텐트에서 데이터 추출
        val selectedCategory = arguments?.getString("category")

        if (selectedCategory == null) {
            activity?.finish()
        } else {
            // 카테고리 상품 로드
            nowPage = 1
            categoryPresenter.loadProducts(true, selectedCategory, nowPage)

            // 리사이클러 뷰
            viewDataBinding.itemContainer.adapter = productAdapter
            val gridLayoutManager = object : GridLayoutManager(activity, 2) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = true
            }
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (productAdapter.getItemViewType(position)) {
                        productAdapter.VIEW_TYPE_ITEM -> {
                            1
                        }

                        productAdapter.VIEW_TYPE_LOADING -> {
                            2
                        }

                        else -> {
                            -1
                        }
                    }
                }
            }
            viewDataBinding.itemContainer.layoutManager = gridLayoutManager

            viewDataBinding.itemContainer.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) {
                        // 제일 끝까지 스크롤 했을 때
                        if (nowPage != -1) {
                            nowPage += 1
                            categoryPresenter.loadMoreProducts(selectedCategory, nowPage)
                        }
                    }
                }
            })
            viewDataBinding.itemContainer.addItemDecoration(productAdapter.RecyclerDecoration())

            // 리사이클러 뷰 바운스 효과 추가
            OverScrollDecoratorHelper.setUpOverScroll(
                viewDataBinding.itemContainer,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL
            )
        }
    }

    override fun onResume() {
        super.onResume()

        // 프래그먼트 전환 시 화면 크기가 달라지는 것을 방지
        viewDataBinding.root.requestLayout()
    }

    override fun getAct() = activity as CategoryActivity

    override fun loadProductsCallback(resultCount: Int) {
        if (resultCount == 0) {
            if (nowPage == 1) {
                // 카테고리에 해당하는 상품이 하나도 없을 때
                viewDataBinding.categoryView.visibility = View.GONE
                viewDataBinding.categoryEmptyView.visibility = View.VISIBLE
            }
            // 더이상 로드할 데이터가 존재하지 않을 때
            nowPage = -1
        } else {
            // 카테고리에 해당하는 상품이 존재할 때
            viewDataBinding.categoryView.visibility = View.VISIBLE
            viewDataBinding.categoryEmptyView.visibility = View.GONE
        }

        // 로딩 뷰 종료
        viewDataBinding.categoryLoadingView.visibility = View.GONE
        viewDataBinding.categoryLoadingImage.pauseAnimation()
    }

    override fun createLoginActivity() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun createProductInBasketResultCallback(count: Int) {
        when (count) {
            0 -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }

            1 -> {
                Snackbar.make(viewDataBinding.root, "장바구니에 상품을 담았습니다.", Snackbar.LENGTH_SHORT)
            }

            20 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "같은 종류의 상품은 최대 20개까지 담을 수 있습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "한 번 더 담으셨네요! \n담긴 수량이 ${count}개가 되었습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }
        }.apply { show() }
    }

    override fun createProductActivity(productItem: ProductItem) {
        val act = activity as CategoryActivity
        val productIntent = Intent(act, ProductActivity::class.java)
        productIntent.putExtra("productItem", productItem)
        act.startActivity(productIntent)
    }
}