package kr.co.younhwan.happybuyer.view.address.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.databinding.RecyclerAddressItemBinding

class AddressViewHolder(
    private val parent: ViewGroup,
    addressItemBinding: RecyclerAddressItemBinding,
    private val listenerFunOfEditBtn: ((AddressItem) -> Unit)?,
) : RecyclerView.ViewHolder(addressItemBinding.root) {

    private val receiver by lazy {
        addressItemBinding.addressItemReceiver
    }

    private val phone by lazy {
        addressItemBinding.addressItemPhone
    }

    private val address by lazy {
        addressItemBinding.addressItemAddress
    }

    private val defaultAddress by lazy {
        addressItemBinding.textView4
    }

    private val selectButton by lazy {
        addressItemBinding.outlinedButton2
    }

    private val editButton by lazy {
        addressItemBinding.outlinedButton
    }

    fun onBind(addressItem: AddressItem, isSelectMode: Boolean) {
        address.text = addressItem.address
        receiver.text = addressItem.addressReceiver
        phone.text = addressItem.addressPhone
        defaultAddress.visibility = View.GONE
        if(addressItem.isDefault == true){
            defaultAddress.visibility = View.VISIBLE
        }

        itemView.setBackgroundResource(R.drawable.bg_address_item)

        selectButton.visibility = View.VISIBLE
        if(!isSelectMode){
            selectButton.visibility = View.GONE
        }

        editButton.setOnClickListener {
            listenerFunOfEditBtn?.invoke(addressItem)
        }
    }

    fun onBindLast(addressItem: AddressItem, isSelectMode: Boolean){
        onBind(addressItem, isSelectMode)
        itemView.setBackgroundResource(R.drawable.bg_address_end_item)
    }
}