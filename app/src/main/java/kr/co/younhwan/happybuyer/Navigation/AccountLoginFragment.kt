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
import kotlinx.android.synthetic.main.fragment_account_login.*
import kr.co.younhwan.happybuyer.MainActivity
import kr.co.younhwan.happybuyer.R
import java.util.concurrent.TimeUnit

class AccountLoginFragment : Fragment() {
    var notEnabledColor: Int? = null
    var enabledColor: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_login, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Activity = activity as MainActivity

        // 인증 문자 보내기 버튼에 관한 설정
        certificationLayout.visibility = View.GONE
        notEnabledColor = ContextCompat.getColor(Activity, R.color.colorDivision)
        enabledColor = ContextCompat.getColor(Activity, R.color.colorTheme)

        submitPhoneNumberBtn.setBackgroundColor(notEnabledColor!!)
        confirmCertificationBtn.setBackgroundColor(notEnabledColor!!)
        submitPhoneNumberBtn.isEnabled = false
        confirmCertificationBtn.isEnabled = false

        // 이벤트 리스너 설정
        phoneNumberInput.editText?.addTextChangedListener(listener1)
        certificationInput.editText?.addTextChangedListener(listener2)

        submitPhoneNumberBtn.setOnClickListener {
            // 파이어베이스 인증 함수 호출
            Activity.sendVerificationCode()
            certificationLayout.visibility = View.VISIBLE
            certificationInput.editText?.requestFocus()
        }
        confirmCertificationBtn.setOnClickListener {
            // otp 확인 함수 호출
            Activity.verifySignInputCode()
        }
    }

    private val listener1 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0?.length == 11) {
                submitPhoneNumberBtn.setBackgroundColor(enabledColor!!)
                submitPhoneNumberBtn.isEnabled = true
            } else {
                submitPhoneNumberBtn.setBackgroundColor(notEnabledColor!!)
                submitPhoneNumberBtn.isEnabled = false
            }
        }
    }

    private val listener2 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0?.length == 6) {
                confirmCertificationBtn.setBackgroundColor(enabledColor!!)
                confirmCertificationBtn.isEnabled = true
            } else {
                confirmCertificationBtn.setBackgroundColor(notEnabledColor!!)
                confirmCertificationBtn.isEnabled = false
            }
        }
    }
}