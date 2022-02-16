package kr.co.younhwan.happybuyer.view.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.databinding.FragmentFavoriteBinding
import kr.co.younhwan.happybuyer.view.main.favorite.presenter.FavoriteContract
import kr.co.younhwan.happybuyer.view.main.favorite.presenter.FavoritePresenter
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomePresenter

class FavoriteFragment: Fragment(), FavoriteContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentFavoriteBinding

    /* Presenter */
    private val favoritePresenter: FavoritePresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        FavoritePresenter(
            this
        )
    }

    /* Method */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = FragmentFavoriteBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}