package kr.co.younhwan.happybuyer.Navigation

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ActionProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.ActivityCategoryBinding
import kr.co.younhwan.happybuyer.databinding.FragmentAccountLoginBinding
import kr.co.younhwan.happybuyer.databinding.FragmentSearchBinding
import java.util.concurrent.TimeUnit

class AccountLoginFragment : Fragment() {
    lateinit var accountLoginFragmentBinding: FragmentAccountLoginBinding

    var notEnabledColor: Int? = null
    var enabledColor: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        accountLoginFragmentBinding = FragmentAccountLoginBinding.inflate(inflater)
        return accountLoginFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val act = activity as MainActivity

        // 인증 문자 보내기 버튼에 관한 설정
        accountLoginFragmentBinding.certificationLayout.visibility = View.GONE
        notEnabledColor = ContextCompat.getColor(act, R.color.colorDivision)
        enabledColor = ContextCompat.getColor(act, R.color.colorTheme)

        accountLoginFragmentBinding.submitPhoneNumberBtn.setBackgroundColor(notEnabledColor!!)
        accountLoginFragmentBinding.confirmCertificationBtn.setBackgroundColor(notEnabledColor!!)
        accountLoginFragmentBinding.submitPhoneNumberBtn.isEnabled = false
        accountLoginFragmentBinding.confirmCertificationBtn.isEnabled = false

        // 이벤트 리스너 설정
        accountLoginFragmentBinding.phoneNumberInput.editText?.addTextChangedListener(listener1)
        accountLoginFragmentBinding.certificationInput.editText?.addTextChangedListener(listener2)

        accountLoginFragmentBinding.submitPhoneNumberBtn.setOnClickListener {
            // 파이어베이스 인증 함수 호출
            act.sendVerificationCode(accountLoginFragmentBinding)
            accountLoginFragmentBinding.certificationLayout.visibility = View.VISIBLE
            accountLoginFragmentBinding.certificationInput.editText?.requestFocus()
        }
        accountLoginFragmentBinding.confirmCertificationBtn.setOnClickListener {
            // otp 확인 함수 호출
            act.verifySignInputCode(accountLoginFragmentBinding)
        }
    }

    private val listener1 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0?.length == 11) {
                accountLoginFragmentBinding.submitPhoneNumberBtn.setBackgroundColor(enabledColor!!)
                accountLoginFragmentBinding.submitPhoneNumberBtn.isEnabled = true
            } else {
                accountLoginFragmentBinding.submitPhoneNumberBtn.setBackgroundColor(notEnabledColor!!)
                accountLoginFragmentBinding.submitPhoneNumberBtn.isEnabled = false
            }
        }
    }

    private val listener2 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0?.length == 6) {
                accountLoginFragmentBinding.confirmCertificationBtn.setBackgroundColor(enabledColor!!)
                accountLoginFragmentBinding.confirmCertificationBtn.isEnabled = true
            } else {
                accountLoginFragmentBinding.confirmCertificationBtn.setBackgroundColor(notEnabledColor!!)
                accountLoginFragmentBinding.confirmCertificationBtn.isEnabled = false
            }
        }
    }
}