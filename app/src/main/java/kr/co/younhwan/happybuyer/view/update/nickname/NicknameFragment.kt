package kr.co.younhwan.happybuyer.view.update.nickname

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.GlobalApplication
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentNicknameBinding
import kr.co.younhwan.happybuyer.view.main.MainActivity
import kr.co.younhwan.happybuyer.view.update.UpdateActivity
import kr.co.younhwan.happybuyer.view.update.nickname.presenter.NicknameContract
import kr.co.younhwan.happybuyer.view.update.nickname.presenter.NicknamePresenter

class NicknameFragment : Fragment(), NicknameContract.View {

    /* View Binding */
    private lateinit var viewDataBinding: FragmentNicknameBinding

    /* Presenter */
    private val nicknamePresenter: NicknamePresenter by lazy {
        NicknamePresenter(
            this,
            userData = UserRepository,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentNicknameBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.editTextTextPersonName.setOnEditorActionListener { textView, i, keyEvent ->

            false
        }

        viewDataBinding.editTextTextPersonName.addTextChangedListener(listener1)

        viewDataBinding.button.setOnClickListener {
            val app = (activity as UpdateActivity).application as GlobalApplication
            val id = app.kakaoAccountId!!

            val newNickname = viewDataBinding.editTextTextPersonName.text.toString()
            nicknamePresenter.updateUserNickname(id, newNickname, app)
        }
    }

    private val listener1 = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null && p0.isNotEmpty()) {
                viewDataBinding.button.run {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorThemeAccent
                        )
                    )
                    isEnabled = true
                }
            } else {
                viewDataBinding.button.run {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorDarkGray
                        )
                    )
                    isEnabled = false
                }

            }
        }
    }

    override fun updateResultCallback(success: Boolean) {
        val act = activity as UpdateActivity

        if (success) {
            act.setResult(Activity.RESULT_OK)
        } else {
            act.setResult(Activity.RESULT_CANCELED)
        }
        act.finish()
    }
}