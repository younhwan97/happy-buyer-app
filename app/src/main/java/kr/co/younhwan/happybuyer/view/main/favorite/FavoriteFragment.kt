package kr.co.younhwan.happybuyer.view.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.younhwan.happybuyer.databinding.FragmentFavoriteBinding

class FavoriteFragment: Fragment() {
    lateinit var favoriteFragmentBinding: FragmentFavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        favoriteFragmentBinding = FragmentFavoriteBinding.inflate(inflater)
        return favoriteFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}