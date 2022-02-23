package kr.co.younhwan.happybuyer.view.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.view.category.adapter.CategoryAdapter
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryContract
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryPresenter
import kr.co.younhwan.happybuyer.view.login.LoginActivity

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
            adapterModel = categoryAdapter,
            adapterView = categoryAdapter
        )
    }

    /* Adapter */
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter()
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

        viewDataBinding.itemContainer.run {
            this.adapter = categoryAdapter
            this.layoutManager = GridLayoutManager(activity as CategoryActivity, 2)
        }
    }

    override fun getAct() = activity as CategoryActivity

    override fun createLoginActivity() =
        startActivity(Intent(requireContext(), LoginActivity::class.java))

    override fun createProductInWishedResultCallback(explain: String) {
        when (explain) {
            "create" -> {
                Snackbar.make(viewDataBinding.root, "관심에 추가되었습니다 :)", Snackbar.LENGTH_SHORT)
                    .show()
            }
            "delete" -> {
                Snackbar.make(viewDataBinding.root, "관심 상품에서 제외되었습니다 :(", Snackbar.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다 :(", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun createProductInBasketResultCallback(isSuccess: Boolean) {
        if (isSuccess) {
            Snackbar.make(viewDataBinding.root, "상품이 장바구니에 추가되었습니다.", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}
