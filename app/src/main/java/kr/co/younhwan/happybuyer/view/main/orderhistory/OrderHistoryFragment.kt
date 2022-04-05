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
        nowPage = 1
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로딩 뷰 셋팅
        viewDataBinding.orderHistoryRecycler.visibility = View.GONE
        viewDataBinding.orderHistoryEmptyView.visibility = View.GONE
        viewDataBinding.orderHistoryLoadingView.visibility = View.VISIBLE
        viewDataBinding.orderHistoryLoadingImage.playAnimation()

        // 주문내역 로드
        orderHistoryPresenter.loadOrderHistory(true, nowPage)

        // 리사이클러뷰
        viewDataBinding.orderHistoryRecycler.adapter = orderHistoryAdapter
        viewDataBinding.orderHistoryRecycler.layoutManager =
            object : LinearLayoutManager(activity) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = true
            }
        viewDataBinding.orderHistoryRecycler.addItemDecoration(orderHistoryAdapter.RecyclerDecoration())
        viewDataBinding.orderHistoryRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 현재 화면에 보이는 항목 중 제일 마지막 항목의 인덱스
                val lastIndex = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                // 리사이클러 뷰가 관리하는 항목의 총 개수
                val count = recyclerView.adapter?.itemCount

                if(lastIndex + 1 == count){
                    // 제일 끝까지 스크롤 했을 때
                    nowPage += 1
                    orderHistoryPresenter.loadOrderHistory(false, nowPage)
                }
            }
        })
    }

    override fun loadOrderHistoryCallback(resultCount: Int) {
        if (resultCount == 0) {
            // 주문 내역이 존재하지 않을 때
            viewDataBinding.orderHistoryRecycler.visibility = View.GONE
            viewDataBinding.orderHistoryEmptyView.visibility = View.VISIBLE
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