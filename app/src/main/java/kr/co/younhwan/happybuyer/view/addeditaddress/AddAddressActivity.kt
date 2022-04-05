package kr.co.younhwan.happybuyer.view.addeditaddress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

        // 로딩 뷰 셋팅
        viewDataBinding.addAddressView.visibility = View.GONE
        viewDataBinding.addAddressLoadingView.visibility = View.VISIBLE
        viewDataBinding.addAddressLoadingImage.playAnimation()

        // 인텐트에서 데이터 추출
        val oldAddressItem = if (intent.hasExtra("address")) {
            // 기존 주소를 수정하는 경우
            intent.getParcelableExtra<AddressItem>("address")
        } else {
            // 새롭게 주소를 추가하는 경우
            AddressItem(
                addressId = -1,
                addressReceiver = "",
                address = "",
                addressPhone = "",
                isDefault = false
            )
        }

        if (oldAddressItem == null) {
            finish()
        } else {
            // 기본 배송지 존재 유무를 확인
            addAddressPresenter.checkHasDefaultAddress()

            // 툴바
            viewDataBinding.addAddressToolbar.setNavigationOnClickListener {
                finish()
            }

            // 받으실 분
            viewDataBinding.addAddressReceiver.editText?.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkValidation()
                }
            })

            if (!oldAddressItem.addressReceiver.isNullOrBlank()) {
                viewDataBinding.addAddressReceiver.editText?.text =
                    Editable.Factory.getInstance().newEditable(oldAddressItem.addressReceiver)
            }

            // 휴대폰
            viewDataBinding.addAddressPhone.editText?.addTextChangedListener(object :
                PhoneNumberFormattingTextWatcher("KR") {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                }

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
                    checkValidation()
                }
            })

            if (!oldAddressItem.addressPhone.isNullOrBlank()) {
                viewDataBinding.addAddressPhone.editText?.text =
                    Editable.Factory.getInstance().newEditable(oldAddressItem.addressPhone)
            }

            // 주소
            viewDataBinding.addAddressAddress.editText?.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkValidation()
                }
            })

            if (!oldAddressItem.address.isNullOrBlank()) {
                viewDataBinding.addAddressAddress.editText?.text =
                    Editable.Factory.getInstance().newEditable(oldAddressItem.address)
            }

            // 기본 배송지 체크 박스
            viewDataBinding.addAddressDefaultAddressCheckBox.setOnCheckedChangeListener { _, _ ->
                checkValidation()
            }

            if (oldAddressItem.isDefault == true) {
                viewDataBinding.addAddressDefaultAddressCheckBox.isChecked = true
                viewDataBinding.addAddressDefaultAddressCheckBox.isClickable = false
                viewDataBinding.addAddressDefaultAddressCheckBox.visibility = View.GONE
            }

            // 저장 버튼
            viewDataBinding.addAddressBtn.isEnabled = false
            viewDataBinding.addAddressBtn.setOnClickListener {
                val receiver = viewDataBinding.addAddressReceiver.editText?.text?.trim().toString()
                val phone = viewDataBinding.addAddressPhone.editText?.text?.trim().toString()
                val address = viewDataBinding.addAddressAddress.editText?.text?.trim().toString()
                val isDefault = viewDataBinding.addAddressDefaultAddressCheckBox.isChecked

                addAddressPresenter.addAddress(
                    AddressItem(
                        addressId = oldAddressItem.addressId,
                        address = address,
                        addressPhone = phone,
                        addressReceiver = receiver,
                        isDefault = isDefault
                    )
                )
            }

            // 삭제 버튼
            viewDataBinding.addAddressDeleteBtn.visibility = View.VISIBLE
            if (oldAddressItem.isDefault == true || oldAddressItem.addressId == -1) {
                viewDataBinding.addAddressDeleteBtn.visibility = View.GONE
                viewDataBinding.addAddressDeleteBtn.isEnabled = false
            }

            viewDataBinding.addAddressDeleteBtn.setOnClickListener {
                addAddressPresenter.deleteAddress(oldAddressItem.addressId)
            }
        }
    }

    override fun getAct() = this

    override fun checkHasDefaultAddressCallback(hasDefaultAddress: Boolean) {
        if (!hasDefaultAddress) {
            // 기본 배송지를 가지고 있지 않은 경우
            viewDataBinding.addAddressDefaultAddressCheckBox.isChecked = true
            viewDataBinding.addAddressDefaultAddressCheckBox.isClickable = false
            viewDataBinding.addAddressDefaultAddressCheckBox.visibility = View.GONE
        }

        // 로딩 뷰 종료
        viewDataBinding.addAddressView.visibility = View.VISIBLE
        viewDataBinding.addAddressLoadingView.visibility = View.GONE
        viewDataBinding.addAddressLoadingImage.pauseAnimation()
    }

    override fun checkValidation() {
        val receiver = viewDataBinding.addAddressReceiver.editText?.text?.trim()
        val phone = viewDataBinding.addAddressPhone.editText?.text?.trim()
        val address = viewDataBinding.addAddressAddress.editText?.text?.trim()

        viewDataBinding.addAddressBtn.isEnabled =
            !(receiver.isNullOrBlank() || phone.isNullOrBlank() || address.isNullOrBlank() || phone.length < 12)
    }

    override fun addAddressCallback(addressItem: AddressItem, isSuccess: Boolean) {
        if (isSuccess) {
            // 정상적으로 주소가 추가되거나 수정되었을 때
            val resultIntent = Intent()
            resultIntent.putExtra("address", addressItem)
            setResult(RESULT_OK, resultIntent)
        } else {
            // 정상적으로 주소가 추가되거나 수정되지 않았을 때
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    override fun deleteAddressCallback(isSuccess: Boolean) {
        if (!isSuccess) {
            // 정상적으로 주소가 삭제되지 않았을 때

        }

        setResult(RESULT_OK)
        finish()
    }
}