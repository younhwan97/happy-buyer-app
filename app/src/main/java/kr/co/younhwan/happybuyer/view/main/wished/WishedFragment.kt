package kr.co.younhwan.happybuyer.view.main.wished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.databinding.FragmentWishedBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.wished.adapter.WishedAdapter
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedContract
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedPresenter

class WishedFragment : Fragment(), WishedContract.View {
    private lateinit var viewDataBinding: FragmentWishedBinding

    private val wishedPresenter: WishedPresenter by lazy {
        WishedPresenter(
            view = this,
            wishedData = WishedRepository,
            basketData = BasketRepository,
            wishedAdapterModel = wishedAdapter,
            wishedAdapterView = wishedAdapter
        )
    }

    private val wishedAdapter: WishedAdapter by lazy {
        WishedAdapter()
    }

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

        wishedPresenter.loadWishedProducts(false)

        // 리사이클러 뷰
        viewDataBinding.wishedRecycler.adapter = wishedAdapter
        viewDataBinding.wishedRecycler.layoutManager = LinearLayoutManager(context)
        viewDataBinding.wishedRecycler.addItemDecoration(wishedAdapter.RecyclerDecoration())
    }

    override fun getAct() = activity as MainActivity

    override fun loadWishedProductsCallback(count: Int) {
        when (count) {
            0 -> {
                viewDataBinding.wishedTopContainer.visibility = View.GONE
                viewDataBinding.wishedEmptyContainer.visibility = View.VISIBLE
                viewDataBinding.wishedRecycler.visibility = View.GONE
            }

            else -> {
                viewDataBinding.wishedTopContainer.visibility = View.VISIBLE
                viewDataBinding.wishedEmptyContainer.visibility = View.GONE
                viewDataBinding.wishedRecycler.visibility = View.VISIBLE

                viewDataBinding.wishedItemCount.text = count.toString()
            }
        }
    }

    override fun deleteWishedProductCallback(perform: String?, resultCount: Int) {
        val snack = when (perform) {
            "delete" -> {
                if (resultCount != 0) {
                    viewDataBinding.wishedItemCount.text = resultCount.toString()
                } else {
                    viewDataBinding.wishedTopContainer.visibility = View.GONE
                    viewDataBinding.wishedEmptyContainer.visibility = View.VISIBLE
                    viewDataBinding.wishedRecycler.visibility = View.GONE
                }

                Snackbar.make(viewDataBinding.root, "상품을 찜 목록에서 제외했습니다.", Snackbar.LENGTH_SHORT)
            }

            else -> {
                Snackbar.make(viewDataBinding.root, "알 수 없는 에러가 발생했습니다.", Snackbar.LENGTH_SHORT)
            }
        }

        snack.setAnchorView(R.id.mainBottomNavigation)
        snack.show()
    }

    override fun addBasketResultCallback(count: Int) {
        val snack = when (count) {
            0 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "알 수 없는 에러가 발생했습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            1 -> {
                Snackbar.make(
                    viewDataBinding.root,
                    "장바구니에 상품을 담았습니다.",
                    Snackbar.LENGTH_SHORT
                )
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
        }

        snack.setAnchorView(R.id.mainBottomNavigation)
        snack.show()
    }
}