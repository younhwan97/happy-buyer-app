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

        viewDataBinding.wishedItemContainer.run {
            this.adapter = wishedAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getAct() = activity as MainActivity

    override fun setEmpty() {
        viewDataBinding.empty.visibility = View.VISIBLE
        viewDataBinding.wishedItemContainer.visibility = View.GONE
    }

    override fun deleteWishedResultCallback() = Snackbar.make(viewDataBinding.root, "관심 상품이 제거되었습니다.", Snackbar.LENGTH_SHORT).show()
}