package kr.co.younhwan.happybuyer.view.main.account.presenter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.view.main.MainActivity

class AccountPresenter(
    private val view: AccountContract.View
) : AccountContract.Model {

    override fun logoutWithKakao(context: Context, activity:MainActivity) {
        val builder = AlertDialog.Builder(context)

        builder.setMessage("로그아웃하시겠습니까?")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                    view.logoutFailCallback(error)
                } else {
                    // 로그아웃 성공
                    val app = activity.application as GlobalApplication
                    app.kakaoAccountId = -1L
                    app.kakaoAccountNickname = ""
                    view.logoutSuccessCallback()
                }
            }
        }
        
        builder.setNegativeButton("닫기", null)
        builder.show()
    }

    override fun withdrawalWithKakao(context: Context, activity: MainActivity) {
        val builder = AlertDialog.Builder(context)

        builder.setMessage("회원 탈퇴하시겠습니까?")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    // 회원탈퇴 실패
                    view.withdrawalFailCallback(error)
                } else {
                    // 회원탈퇴 성공
                    view.withdrawalSuccessCallback()
                }
            }
        }
        
        builder.setNegativeButton("닫기", null)
        builder.show()
    }
}