package kr.co.younhwan.happybuyer.view.main.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.databinding.FragmentAccountBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.account.presenter.AccountContract
import kr.co.younhwan.happybuyer.view.main.account.presenter.AccountPresenter
import kr.co.younhwan.happybuyer.view.update.UpdateActivity

class AccountFragment : Fragment(), AccountContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentAccountBinding

    /* Presenter */
    private val accountPresenter: AccountPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        AccountPresenter(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentAccountBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as MainActivity
        val app = act.application as GlobalApplication

        // set view
        viewDataBinding.nickname.text =
            if (app.kakaoAccountNickname != null) app.kakaoAccountNickname else "${app.kakaoAccountId}"

        viewDataBinding.nicknameInProfile.text =
            if (app.kakaoAccountNickname != null) app.kakaoAccountNickname else "${app.kakaoAccountId}"


        // set event listener
        viewDataBinding.callBtn.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:031-654-3320")
            startActivity(dialIntent)
        }

        viewDataBinding.logoutBtn.setOnClickListener {
            accountPresenter.logoutWithKakao(requireContext(), act)
        }

        viewDataBinding.withdrawalBtn.setOnClickListener {
            accountPresenter.withdrawalWithKakao(requireContext(), act)
        }

        viewDataBinding.profileNicknameContainer.setOnClickListener{
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "nickname")
            startActivity(updateIntent)
        }

        viewDataBinding.profilePointNumberContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "pointNumber")
            startActivity(updateIntent)
        }

        viewDataBinding.profilePhoneNumberContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "phoneNumber")
            startActivity(updateIntent)
        }
    }

    override fun logoutFailCallback(error: Throwable?) {
        Log.e("kakao", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
    }

    override fun logoutSuccessCallback() {
        val act = activity as MainActivity
        Toast.makeText(context, "로그아웃에 성공하셨습니다.", Toast.LENGTH_SHORT)
            .show()
        val mainIntent = Intent(context, MainActivity::class.java)
        act.finish()
        startActivity(mainIntent)
    }

    override fun withdrawalFailCallback(error: Throwable?) {
        Log.e("kakao", "연결 끊기 실패", error)
    }

    override fun withdrawalSuccessCallback() {
        Log.i("kakao", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
    }
}