package kr.co.younhwan.happybuyer.Navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.databinding.FragmentHomeBinding
import kr.co.younhwan.happybuyer.databinding.FragmentSearchBinding

class SearchFragment:Fragment() {
    // View Binding
    lateinit var searchFragmentBinding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        searchFragmentBinding = FragmentSearchBinding.inflate(inflater)
        return searchFragmentBinding.root
    }
}