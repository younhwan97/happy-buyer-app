package kr.co.younhwan.happybuyer.view.main.orderhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.OrderItem
import kr.co.younhwan.happybuyer.data.source.order.OrderRepository
import kr.co.younhwan.happybuyer.databinding.FragmentOrderHistoryBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.orderhistory.adapter.OrderHistoryAdapter
import kr.co.younhwan.happybuyer.view.main.orderhistory.presenter.OrderHistoryContract
import kr.co.younhwan.happybuyer.view.main.orderhistory.presenter.OrderHistoryPresenter
import kr.co.younhwan.happybuyer.view.orderdetail.OrderDetailActivity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class OrderHistoryFragment : Fragment(), OrderHistoryContract.View {
    private lateinit var viewDataBinding: FragmentOrderHistoryBinding

    private val orderHistoryPresenter: OrderHistoryPresenter by lazy {
        OrderHistoryPresenter(
            view = this,
            orderData = OrderRepository,
            orderHistoryAdapterModel = orderHistoryAdapter,
            orderHistoryAdapterView = orderHistoryAdapter
        )
    }

    private val orderHistoryAdapter: OrderHistoryAdapter by lazy {
        OrderHistoryAdapter()
    }

    private var nowPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentOrderHistoryBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로딩 뷰 셋팅
        viewDataBinding.orderHistoryRecycler.visibility = View.GONE
        viewDataBinding.orderHistoryEmptyView.visibility = View.GONE
        viewDataBinding.orderHistoryLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderHistoryLoadingImage.playAnimation()

        // (첫번째 페이지) 주문내역 로드
        nowPage = 1
        orderHistoryPresenter.loadOrderHistory(true, nowPage)

        // 주문 내역 리사이클러 뷰
        viewDataBinding.orderHistoryRecycler.adapter = orderHistoryAdapter
        viewDataBinding.orderHistoryRecycler.layoutManager =
            object : LinearLayoutManager(activity) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = true
            }
        viewDataBinding.orderHistoryRecycler.addItemDecoration(orderHistoryAdapter.RecyclerDecoration())
        OverScrollDecoratorHelper.setUpOverScroll(
            viewDataBinding.orderHistoryRecycler,
            OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )

        // 주문 내역 리사이클러 뷰 스크롤 이벤트 리스너
        viewDataBinding.orderHistoryRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1)) {
                    // 제일 끝까지 스크롤 했을 때
                    if (nowPage != -1) {
                        nowPage += 1
                        orderHistoryPresenter.loadMoreOrderHistory(nowPage)
                    }
                }
            }
        })
    }

    override fun loadOrderHistoryCallback(resultCount: Int) {
        if (resultCount == 0) {
            if (nowPage == 1) {
                // 주문 내역이 하나도 존재하지 않을 때 (= Empty view 를 나타낸다.)
                viewDataBinding.orderHistoryRecycler.visibility = View.GONE
                viewDataBinding.orderHistoryEmptyView.visibility = View.VISIBLE
            }
            // 주문 내역은 있고, 더이상 로드할 데이터가 존재하지 않을 때
            nowPage = -1
        } else {
            // 주문 내역이 존재할 때
            viewDataBinding.orderHistoryRecycler.visibility = View.VISIBLE
            viewDataBinding.orderHistoryEmptyView.visibility = View.GONE
        }

        // 로딩 뷰 종료
        viewDataBinding.orderHistoryLoadingView.visibility = View.GONE
        viewDataBinding.orderHistoryLoadingImage.pauseAnimation()
    }

    override fun getAct() = activity as MainActivity

    override fun createOrderDetailAct(orderHistoryItem: OrderItem) {
        val orderDetailIntent = Intent(requireContext(), OrderDetailActivity::class.java)
        orderDetailIntent.putExtra("order", orderHistoryItem)
        startActivity(orderDetailIntent)
    }
}