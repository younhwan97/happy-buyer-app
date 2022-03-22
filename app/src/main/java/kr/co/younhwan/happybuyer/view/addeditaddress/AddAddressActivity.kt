package kr.co.younhwan.happybuyer.view.addeditaddress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.databinding.ActivityAddAddressBinding

class AddAddressActivity : AppCompatActivity(), AddAddressContract.View {
    lateinit var viewDataBinding: ActivityAddAddressBinding

    private val addAddressPresenter: AddAddressPresenter by lazy {
        AddAddressPresenter(
            view = this,
            addressData = AddressRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 툴바
        viewDataBinding.addAddressToolbar.setNavigationOnClickListener {
            finish()
        }

        // 받으실 분
        viewDataBinding.addAddressReceiver.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkValidation()
            }
        })

        // 휴대폰
        viewDataBinding.addAddressPhone.editText?.addTextChangedListener(object :
            PhoneNumberFormattingTextWatcher("KR") {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                super.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                checkValidation()
            }
        })

        // 주소
        viewDataBinding.addAddressAddress.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkValidation()
            }
        })

        // 저장 버튼
        viewDataBinding.addAddressBtn.isEnabled = false
        viewDataBinding.addAddressBtn.setOnClickListener {
            val receiver = viewDataBinding.addAddressReceiver.editText?.text?.trim().toString()
            val phone = viewDataBinding.addAddressPhone.editText?.text?.trim().toString()
            val address = viewDataBinding.addAddressAddress.editText?.text?.trim().toString()

            addAddressPresenter.addAddress(
                addressItem = AddressItem(
                    addressId = -1,
                    address = address,
                    addressPhone = phone,
                    addressReceiver = receiver
                )
            )
        }
    }

    override fun getAct() = this

    override fun checkValidation() {
        var passValidationCheck = true

        val receiver = viewDataBinding.addAddressReceiver.editText?.text?.trim()
        val phone = viewDataBinding.addAddressPhone.editText?.text?.trim()
        val address = viewDataBinding.addAddressAddress.editText?.text?.trim()

        if (receiver.isNullOrBlank()) {
            passValidationCheck = false
        }

        if (phone.isNullOrBlank() || phone.length < 11) {
            passValidationCheck = false
        }

        if (address.isNullOrBlank()) {
            passValidationCheck = false
        }

        viewDataBinding.addAddressBtn.isEnabled = passValidationCheck
    }

    override fun addAddressCallback(addressId: Int, addressItem: AddressItem) {
        if(addressId != -1){ // 정상적으로 디비에 새로운 배송지가 추가되었을 때
            val resultIntent = Intent()
            resultIntent.putExtra("address_id", addressId)
            resultIntent.putExtra("receiver", addressItem.addressReceiver)
            resultIntent.putExtra("phone", addressItem.addressPhone)
            resultIntent.putExtra("address", addressItem.address)
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            setResult(RESULT_CANCELED)
        }
    }
}