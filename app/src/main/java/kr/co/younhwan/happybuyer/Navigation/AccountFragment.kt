package kr.co.younhwan.happybuyer.Navigation

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kakao.sdk.user.UserApiClient
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    lateinit var accountFragmentBinding : FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        accountFragmentBinding = FragmentAccountBinding.inflate(inflater)
        return accountFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as MainActivity
        val pref = act.getSharedPreferences("account", AppCompatActivity.MODE_PRIVATE)
        val account = pref.getString("account", "")

//        if (account.isNullOrEmpty())
//            act.setFragment("home")
//        else
//            accountFragmentBinding.accountInfo.text = account

        if(act.kakaoAccountId != null){

        }else{
            act.setFragment("home")
        }

        accountFragmentBinding.logoutBtn.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("kakao", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Toast.makeText(requireContext(), "로그아웃에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                    val main_intent = Intent(requireContext(), MainActivity::class.java)
                    act.finish()
                    startActivity(main_intent)
                }
            }
        }
    }
}