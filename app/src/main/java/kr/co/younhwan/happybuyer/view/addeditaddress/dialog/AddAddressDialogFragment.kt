package kr.co.younhwan.happybuyer.view.addeditaddress.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.co.younhwan.happybuyer.databinding.FragmentAddAddressDialogBinding

class AddAddressDialogFragment : DialogFragment() {
    lateinit var viewDataBinding: FragmentAddAddressDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentAddAddressDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return viewDataBinding.root
    }
}