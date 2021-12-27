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
import kotlinx.android.synthetic.main.fragment_account.*
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Activity = activity as MainActivity
        val pref = Activity.getSharedPreferences("account", AppCompatActivity.MODE_PRIVATE)
        val account = pref.getString("account", "")

        if (account.isNullOrEmpty())
            Activity.setFragment("home")
        else
            accountInfo.text = account

        logoutBtn.setOnClickListener {
            Activity.logout()
        }
    }
}