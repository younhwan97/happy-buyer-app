package kr.co.younhwan.happybuyer.view.update.nickname

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentNicknameBinding
import kr.co.younhwan.happybuyer.view.update.UpdateActivity
import kr.co.younhwan.happybuyer.view.update.nickname.presenter.NicknameContract
import kr.co.younhwan.happybuyer.view.update.nickname.presenter.NicknamePresenter

class NicknameFragment : Fragment(), NicknameContract.View {
    private lateinit var viewDataBinding: FragmentNicknameBinding

    private val nicknamePresenter: NicknamePresenter by lazy {
        NicknamePresenter(
            view = this,
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

        // 에딧 텍스트
        viewDataBinding.nicknameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.isNotEmpty()) {
                    viewDataBinding.nicknameBtn.isEnabled = true
                    viewDataBinding.nicknameBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.fontColorBlack
                        )
                    )
                } else {
                    viewDataBinding.nicknameBtn.isEnabled = false
                    viewDataBinding.nicknameBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.fontColorWhite
                        )
                    )
                }
            }
        })

        // 저장 버튼
        viewDataBinding.nicknameBtn.isEnabled = false
        viewDataBinding.nicknameBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.fontColorWhite
            )
        )
        viewDataBinding.nicknameBtn.setOnClickListener {
            val newNickname = viewDataBinding.nicknameEditText.text.toString()
            nicknamePresenter.updateUserNickname(newNickname)
        }
    }

    override fun getAct() = activity as UpdateActivity

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