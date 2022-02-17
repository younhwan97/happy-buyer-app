package kr.co.younhwan.happybuyer.view.update.nickname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.databinding.FragmentNicknameBinding

class NicknameFragment : Fragment(){

    /* View Binding */
    private lateinit var viewDataBinding: FragmentNicknameBinding


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
    }
}