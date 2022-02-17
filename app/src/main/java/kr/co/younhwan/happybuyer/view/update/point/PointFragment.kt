package kr.co.younhwan.happybuyer.view.update.point

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.databinding.FragmentNicknameBinding
import kr.co.younhwan.happybuyer.databinding.FragmentPointBinding

class PointFragment : Fragment(){

    /* View Binding */
    private lateinit var viewDataBinding: FragmentPointBinding


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
    }
}