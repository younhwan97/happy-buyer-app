package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.adapter.product.ProductAdapter
import kr.co.younhwan.happybuyer.data.ProductItem
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryContract
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryPresenter
import kr.co.younhwan.happybuyer.view.login.LoginActivity
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.product.ProductActivity

class CategoryFragment : Fragment(), CategoryContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentCategoryBinding

    /* Presenter */
    private val categoryPresenter: CategoryPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        CategoryPresenter(
            this,
            productData = ProductRepository,
            userData = UserRepository,
            adapterModel = productAdapter,
            adapterView = productAdapter
        )
    }

    /* Adapter */
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter("category")
    }

    /* Data */
    private lateinit var selectedCateogory: String

    /* Method */
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

        selectedCateogory = arguments?.getString("category").toString()

        categoryPresenter.loadProducts(false, selectedCateogory)

        viewDataBinding.run {
            itemContainer.adapter = productAdapter
            itemContainer.layoutManager = GridLayoutManager(activity as CategoryActivity, 2)
            itemContainer.addItemDecoration(productAdapter.RecyclerDecoration())

            categoryItemCountText.text =
                "총".plus(" ").plus(productAdapter.itemCount.toString()).plus("개")

            categorySortBtn.setOnClickListener {

            }
        }
    }


    override fun getAct() = activity as CategoryActivity

    override fun createLoginActivity() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun createProductInBasketResultCallback(count: Int) {
        when(count){
            0 -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }

            1 -> {
                Snackbar.make(viewDataBinding.root, "장바구니에 상품을 담았습니다.", Snackbar.LENGTH_SHORT)
            }

            10 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "같은 종류의 상품은 최대 10개까지 담을 수 있습니다.",
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
