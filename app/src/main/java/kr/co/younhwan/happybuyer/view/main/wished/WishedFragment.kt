package kr.co.younhwan.happybuyer.view.main.wished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.FragmentWishedBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.wished.adapter.WishedAdapter
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedContract
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedPresenter

class WishedFragment : Fragment(), WishedContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentWishedBinding

    /* Presenter */
    private val wishedPresenter: WishedPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        WishedPresenter(
            this,
            productData = ProductRepository,
            basketData = BasketRepository,
            adapterModel = wishedAdapter,
            adapterView = wishedAdapter
        )
    }

    private val wishedAdapter: WishedAdapter by lazy {
        WishedAdapter()
    }

    /* Method */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentWishedBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishedPresenter.loadWishedItem(requireContext(), false)

        viewDataBinding.wishedRecycler.run {
            this.adapter = wishedAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(wishedAdapter.RecyclerDecoration())
        }
    }

    override fun getAct() = activity as MainActivity

    override fun setEmpty() {
        viewDataBinding.wishedEmptyContainer.visibility = View.VISIBLE
        viewDataBinding.wishedRecycler.visibility = View.GONE
    }

    override fun deleteWishedResultCallback(perform: String?){
        when(perform){
            "delete" -> {
                Snackbar.make(viewDataBinding.root, "상품을 찜 목록에서 제외했습니다.", Snackbar.LENGTH_SHORT)
            }

            else -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }
        }.apply {
            setAnchorView(R.id.mainBottomNavigation)
            show()
        }
    }

    override fun addBasketResultCallback(count: Int) {
        when(count){
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
        }.apply {
            setAnchorView(R.id.mainBottomNavigation)
            show()
        }
    }
}