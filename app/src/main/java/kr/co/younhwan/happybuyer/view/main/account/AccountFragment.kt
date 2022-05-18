package kr.co.younhwan.happybuyer.view.main.account

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.databinding.FragmentAccountBinding
import kr.co.younhwan.happybuyer.view.address.AddressActivity
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.main.account.presenter.AccountContract
import kr.co.younhwan.happybuyer.view.main.account.presenter.AccountPresenter
import kr.co.younhwan.happybuyer.view.splash.SplashActivity
import kr.co.younhwan.happybuyer.view.update.UpdateActivity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class AccountFragment : Fragment(), AccountContract.View {
    private lateinit var viewDataBinding: FragmentAccountBinding

    private val accountPresenter: AccountPresenter by lazy {
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

        // 엑티비티 및 어플리케이션
        val act = activity as MainActivity
        val app = act.application as GlobalApplication

        // 전체 컨테이너
        OverScrollDecoratorHelper.setUpOverScroll(viewDataBinding.accountContentContainer)

        // 유저 닉네임
        viewDataBinding.accountNickname.text =
            if (app.nickname != null) app.nickname else "${app.kakaoAccountId}"

        // 배송지 관리 및 문의하기 버튼
        viewDataBinding.accountAddressBtn.setOnClickListener {
            val addressIntent = Intent(context, AddressActivity::class.java)
            addressIntent.putExtra("is_select_mode", false)
            startActivity(addressIntent)
        }

        viewDataBinding.accountInquiryBtn.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:031-654-3320")
            startActivity(dialIntent)
        }

        // 프로필
        viewDataBinding.accountProfileNickname.text =
            if (app.nickname != null && app.nickname != "null") app.nickname else "${app.kakaoAccountId}"

        viewDataBinding.accountProfilePoint.text =
            if (app.point != null && app.point != "null") app.point else "-"

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    viewDataBinding.accountNickname.text = app.nickname
                    viewDataBinding.accountProfileNickname.text = app.nickname
                    viewDataBinding.accountProfilePoint.text = app.point
                }
            }

        viewDataBinding.accountProfileNicknameContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("update_target", "nickname")
            startForResult.launch(updateIntent)
        }

        viewDataBinding.accountProfilePointContainer.setOnClickListener {
            val updateIntent = Intent(requireContext(), UpdateActivity::class.java)
            updateIntent.putExtra("update_target", "point")
            startForResult.launch(updateIntent)
        }

        // 알림 설정
        viewDataBinding.accountNotificationGet.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){

            } else {

            }
        }

        // 기타 (회원 탈퇴 및 로그아웃 버튼)
        viewDataBinding.accountEctLogoutBtn.setOnClickListener {
            accountPresenter.logoutWithKakao()
        }

        viewDataBinding.accountEctWithdrawalBtn.setOnClickListener {
            accountPresenter.withdrawalWithKakao()
        }
    }

    override fun getAct() = activity as MainActivity

    override fun logoutResultCallback(success: Boolean, error: Throwable?) {
        if (success) {
            // 토스트 메세지 출력
            Toast.makeText(context, "로그아웃에 성공하셨습니다.", Toast.LENGTH_LONG)
                .show()

            // 메인 엑티비티 실행
            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
        } else {
            Toast.makeText(context, "로그아웃에 실패하셨습니다.", Toast.LENGTH_LONG)
                .show()

            // 스팰리쉬 엑티비티 실행
            val splashIntent = Intent(context, SplashActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(splashIntent)
        }
    }

    override fun withdrawalResultCallback(success: Boolean, error: Throwable?) {
        if (success) {
            // 토스트 메세지 출력
            Toast.makeText(context, "회원탈퇴에 성공하셨습니다.", Toast.LENGTH_SHORT)
                .show()

            // 메인 엑티비티 실행
            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainIntent)
        } else {
            Toast.makeText(context, "회원탈퇴에 실패하셨습니다.", Toast.LENGTH_LONG)
                .show()

            // 스팰리쉬 엑티비티 실행
            val splashIntent = Intent(context, SplashActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(splashIntent)
        }
    }
}