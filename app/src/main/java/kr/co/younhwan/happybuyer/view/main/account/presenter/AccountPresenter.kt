package kr.co.younhwan.happybuyer.view.main.account.presenter

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R

class AccountPresenter(
    private val view: AccountContract.View
) : AccountContract.Model {

    private val act = view.getAct()
    private val app = act.application as GlobalApplication

    override fun logoutWithKakao() {
        MaterialAlertDialogBuilder(act)
            .setTitle(act.resources.getString(R.string.account_etc_logout_text))
            .setMessage(act.resources.getString(R.string.account_etc_logout_message))
            .setNegativeButton(act.resources.getString(R.string.account_etc_logout_cancel_btn_text)) { _, _ ->
                // Nothing to do
            }
            .setPositiveButton(act.resources.getString(R.string.account_etc_logout_confirm_btn_text)) { dialog, which ->
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        // 로그아웃 실패
                        view.logoutWithKakaoCallback(false, error)
                    } else {
                        // 로그아웃 성공
                        // 어플리케이션 데이터 셋팅
                        app.isLogined = false
                        app.kakaoAccountId = -1L
                        app.nickname = null
                        app.point = null
                        app.wishedProductId = ArrayList()
                        app.basketItemCount = 0

                        view.logoutWithKakaoCallback(true, null)
                    }
                }
            }
            .show()
    }

    override fun withdrawalWithKakao() {
        MaterialAlertDialogBuilder(act)
            .setTitle(act.resources.getString(R.string.account_etc_withdrawal_text))
            .setMessage(act.resources.getString(R.string.account_etc_withdrawal_message))
            .setNegativeButton(act.resources.getString(R.string.account_etc_withdrawal_cancel_btn_text)) { _, _ ->
                // Nothing to do
            }
            .setPositiveButton(act.resources.getString(R.string.account_etc_withdrawal_confirm_btn_text)) { dialog, which ->
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        // 회원탈퇴 실패
                        view.withdrawalWithKakaoCallback(false, error)
                    } else {
                        // 회원탈퇴 성공
                        // 어플리케이션 데이터 셋팅
                        app.isLogined = false
                        app.kakaoAccountId = -1L
                        app.nickname = null
                        app.point = null
                        app.wishedProductId = ArrayList()
                        app.basketItemCount = 0

                        view.withdrawalWithKakaoCallback(true, null)
                    }
                }
            }
            .show()
    }
}