package kr.co.younhwan.happybuyer.view.order.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.co.younhwan.happybuyer.databinding.FragmentOrderDialogBinding

class OrderDialogFragment : DialogFragment() {
    lateinit var viewDataBinding: FragmentOrderDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentOrderDialogBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return viewDataBinding.root
    }
}