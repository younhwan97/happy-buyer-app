package kr.co.younhwan.happybuyer.Navigation

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.FragmentAccountBinding
import kr.co.younhwan.happybuyer.databinding.FragmentAccountLoginBinding

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

        if (account.isNullOrEmpty())
            act.setFragment("home")
        else
            accountFragmentBinding.accountInfo.text = account

        accountFragmentBinding.logoutBtn.setOnClickListener {
            act.logout()
        }
    }
}