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
    private val listenerFunOfSelectBtn: ((AddressItem) -> Unit)?
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
        addressItemBinding.addressDefaultBadge
    }

    private val selectButton by lazy {
        addressItemBinding.outlinedButton2
    }

    private val editButton by lazy {
        addressItemBinding.outlineButton
    }

    fun onBind(addressItem: AddressItem, isSelectMode: Boolean) {
        // 테두리
        itemView.setBackgroundResource(R.drawable.bg_address_item)
        
        // 기본 배송지 뱃지
        defaultAddress.visibility = View.GONE
        if (addressItem.isDefault == true) {
            defaultAddress.visibility = View.VISIBLE
        }
        
        // 주소
        address.text = addressItem.address

        // 받으실 분
        receiver.text = addressItem.addressReceiver

        // 휴대폰
        phone.text = addressItem.addressPhone
        
        // 수정 버튼
        editButton.setOnClickListener {
            listenerFunOfEditBtn?.invoke(addressItem)
        }

        // 선택 버튼
        selectButton.visibility = View.VISIBLE
        if (!isSelectMode) {
            selectButton.visibility = View.GONE
        }
        selectButton.setOnClickListener {
            listenerFunOfSelectBtn?.invoke(addressItem)
        }
    }

    fun onBindLast(addressItem: AddressItem, isSelectMode: Boolean) {
        onBind(addressItem, isSelectMode)
        
        // 테두리
        itemView.setBackgroundResource(R.drawable.bg_address_end_item)
    }
}