package kr.co.younhwan.happybuyer.view.update.point

import android.app.Activity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.source.user.UserRepository
import kr.co.younhwan.happybuyer.databinding.FragmentPointBinding
import kr.co.younhwan.happybuyer.view.update.UpdateActivity
import kr.co.younhwan.happybuyer.view.update.point.presenter.PointContract
import kr.co.younhwan.happybuyer.view.update.point.presenter.PointPresenter

class PointFragment : Fragment(), PointContract.View {
    private lateinit var viewDataBinding: FragmentPointBinding

    private val pointPresenter: PointPresenter by lazy {
        PointPresenter(
            view = this,
            userData = UserRepository,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentPointBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 에딧 텍스트
        viewDataBinding.pointEditText.addTextChangedListener(object :
            PhoneNumberFormattingTextWatcher("KR") {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                super.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s != null && s.isNotEmpty()) {
                    viewDataBinding.pointBtn.isEnabled = true
                    viewDataBinding.pointBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.fontColorBlack
                        )
                    )
                } else {
                    viewDataBinding.pointBtn.isEnabled = false
                    viewDataBinding.pointBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.fontColorWhite
                        )
                    )
                }
            }
        })

        // 저장 버튼
        viewDataBinding.pointBtn.isEnabled = false
        viewDataBinding.pointBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.fontColorWhite
            )
        )
        viewDataBinding.pointBtn.setOnClickListener {
            val newPoint = viewDataBinding.pointEditText.text.toString()
            pointPresenter.updateUserPoint(newPoint)
        }
    }

    override fun getAct() = activity as UpdateActivity

    override fun updateUserPointCallback(isSuccess: Boolean) {
        val act = activity as UpdateActivity

        if (isSuccess) {
            act.setResult(Activity.RESULT_OK)
        } else {
            act.setResult(Activity.RESULT_CANCELED)
        }

        act.finish()
    }
}