package kr.co.younhwan.happybuyer.view.main.account

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
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
        viewDataBinding.accountNickname.text =
            if (app.nickname != null) app.nickname else "${app.kakaoAccountId}"

        viewDataBinding.accountProfileNickname.text =
            if (app.nickname != null) app.nickname else "${app.kakaoAccountId}"


        // set event listener
        viewDataBinding.accountTopThirdBtn.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:031-654-3320")
            startActivity(dialIntent)
        }

        viewDataBinding.accountEctLogoutBtn.setOnClickListener {
            accountPresenter.logoutWithKakao(requireContext(), act)
        }

        viewDataBinding.accountEctWithdrawalBtn.setOnClickListener {
            accountPresenter.withdrawalWithKakao(requireContext(), act)
        }

        val startForResult2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val app = activity?.application as GlobalApplication
                    viewDataBinding.accountProfileNickname.text = app.nickname
                    viewDataBinding.accountNickname.text = app.nickname
                }
            }

        viewDataBinding.accountProfileNicknameContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "nickname")
            startForResult2.launch(updateIntent)
        }

        viewDataBinding.accountProfilePointContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "pointNumber")
            startActivity(updateIntent)
        }

        viewDataBinding.accountProfilePhoneContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("target", "phoneNumber")
            startActivity(updateIntent)
        }
    }

    override fun logoutResultCallback(success: Boolean, error: Throwable?) {
        if (success) {
            val act = activity as MainActivity
            Toast.makeText(context, "로그아웃에 성공하셨습니다.", Toast.LENGTH_SHORT)
                .show()
            val mainIntent = Intent(context, MainActivity::class.java)
            act.finish()
            startActivity(mainIntent)
        } else {
            Log.e("kakao", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
        }
    }

    override fun withdrawalResultCallback(success: Boolean, error: Throwable?) {
        if (success) {
            Log.e("kakao", "연결 끊기 실패", error)
        } else {
            Log.i("kakao", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
        }
    }
}