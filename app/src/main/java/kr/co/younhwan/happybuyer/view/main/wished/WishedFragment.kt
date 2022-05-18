package kr.co.younhwan.happybuyer.view.main.wished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.basket.BasketRepository
import kr.co.younhwan.happybuyer.data.source.wished.WishedRepository
import kr.co.younhwan.happybuyer.databinding.FragmentWishedBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.wished.adapter.WishedAdapter
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedContract
import kr.co.younhwan.happybuyer.view.main.wished.presenter.WishedPresenter
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

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

        // 로딩 뷰 셋팅
        setLoadingView()

        // 찜한 상품 로드
        wishedPresenter.loadWishedProducts(false)

        // 찜한 상품 리사이클러뷰
        viewDataBinding.wishedRecycler.adapter = wishedAdapter
        viewDataBinding.wishedRecycler.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = true
        }
        viewDataBinding.wishedRecycler.addItemDecoration(wishedAdapter.RecyclerDecoration())

        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.wishedRecycler,
            OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
    }

    private fun setLoadingView() {
        viewDataBinding.wishedItemCountContainer.visibility = View.GONE
        viewDataBinding.wishedRecycler.visibility = View.GONE
        viewDataBinding.wishedEmptyView.visibility = View.GONE
        viewDataBinding.wishedLoadingView.visibility = View.VISIBLE
        viewDataBinding.wishedLoadingImage.playAnimation()
    }

    override fun getAct() = activity as MainActivity

    override fun loadWishedProductsCallback(resultCount: Int) {
        if (resultCount == 0) {
            // 찜한 상품이 없을 때
            viewDataBinding.wishedEmptyView.visibility = View.VISIBLE
            viewDataBinding.wishedItemCountContainer.visibility = View.GONE
            viewDataBinding.wishedRecycler.visibility = View.GONE
        } else {
            // 찜한 상품이 있을 때
            viewDataBinding.wishedEmptyView.visibility = View.GONE
            viewDataBinding.wishedItemCountContainer.visibility = View.VISIBLE
            viewDataBinding.wishedRecycler.visibility = View.VISIBLE

            viewDataBinding.wishedItemCount.text = resultCount.toString()
        }

        // 로딩 뷰 종료
        viewDataBinding.wishedLoadingView.visibility = View.GONE
        viewDataBinding.wishedLoadingImage.pauseAnimation()
    }

    override fun deleteProductInWishedCallback(perform: String?, resultCount: Int) {
        val snack = when (perform) {
            "delete" -> {
                if (resultCount != 0) {
                    viewDataBinding.wishedItemCount.text = resultCount.toString()
                } else {
                    viewDataBinding.wishedEmptyView.visibility = View.VISIBLE
                    viewDataBinding.wishedItemCountContainer.visibility = View.GONE
                    viewDataBinding.wishedRecycler.visibility = View.GONE
                }

                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root, 
                    "상품을 찜 목록에서 제외했습니다.", 
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root, 
                    "알 수 없는 에러가 발생했습니다.", 
                    Snackbar.LENGTH_SHORT
                )
            }
        }

        snack.setAnchorView(R.id.mainBottomNavigation)
        snack.show()
    }

    override fun createOrUpdateProductInBasketCallback(resultCount: Int) {
        val snack = when (resultCount) {
            1 -> {
                // 상품이 기존에 존재하지 않았으나 처음 추가되었을 때
                val act = activity as MainActivity
                val app = act.application as GlobalApplication

                app.basketItemCount += 1

                if (app.basketItemCount == 1) {
                    act.setNotificationBadge()
                }

                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "장바구니에 상품을 담았습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            in 2..19 -> {
                // 기존에 장바구니에 존재한 상품이 갯수만 늘었을 때
                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "한 번 더 담으셨네요! \n담긴 수량이 ${resultCount}개가 되었습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            20 -> {
                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "같은 종류의 상품은 최대 20개까지 담을 수 있습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }

            else -> {
                // 스낵바 리턴
                Snackbar.make(
                    viewDataBinding.root,
                    "알 수 없는 에러가 발생했습니다.",
                    Snackbar.LENGTH_SHORT
                )
            }
        }

        snack.setAnchorView(R.id.mainBottomNavigation)
        snack.show()
    }
}