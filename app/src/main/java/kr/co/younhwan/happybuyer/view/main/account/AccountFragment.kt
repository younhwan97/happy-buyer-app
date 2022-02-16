package kr.co.younhwan.happybuyer.view.main.account

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.databinding.FragmentAccountBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity

class AccountFragment : Fragment() {
    private lateinit var accountFragmentBinding : FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        accountFragmentBinding = FragmentAccountBinding.inflate(inflater)
        return accountFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as MainActivity

        // accountFragmentBinding.kakaoId.text = if(act.kakaoAccountNickname != null) act.kakaoAccountNickname else "${act.kakaoAccountId}"

        accountFragmentBinding.logoutBtn.setOnClickListener {
            var builder = AlertDialog.Builder(requireContext())

            builder.setMessage("로그아웃하시겠습니까?")
            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e("kakao", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    }
                    else {
                        val app = (activity as MainActivity).application as GlobalApplication
                        app.kakaoAccountId  = -1L
                        app.kakaoAccountNickname = ""
                        Toast.makeText(requireContext(), "로그아웃에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                        val mainIntent = Intent(requireContext(), MainActivity::class.java)
                        act.finish()
                        startActivity(mainIntent)
                    }
                }
            }
            builder.setNegativeButton("닫기", null)
            builder.show()
        }
        accountFragmentBinding.disconnectBtn.setOnClickListener {
            var builder = AlertDialog.Builder(requireContext())
            builder.setMessage("회원 탈퇴하시겠습니까?")
            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                UserApiClient.instance.unlink { error ->
                    if (error != null) {
                        Log.e("kakao", "연결 끊기 실패", error)
                    }
                    else {
                        Log.i("kakao", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                    }
                }
            }
            builder.setNegativeButton("닫기", null)
            builder.show()
        }
    }
}