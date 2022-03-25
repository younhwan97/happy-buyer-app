package kr.co.younhwan.happybuyer.view.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderBinding
import kr.co.younhwan.happybuyer.view.addeditaddress.AddAddressActivity

class OrderActivity : AppCompatActivity(), OrderContract.View {
    lateinit var viewDataBinding: ActivityOrderBinding

    private val orderPresenter: OrderPresenter by lazy {
        OrderPresenter(
            view = this,
            addressData = AddressRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        orderPresenter.loadDefaultAddress()

        // 배송지
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    orderPresenter.loadDefaultAddress()
                }
            }

        viewDataBinding.orderAddressEmptyText.setOnClickListener {
            val addAddressIntent = Intent(this, AddAddressActivity::class.java)
            startForResult.launch(addAddressIntent)
        }
    }

    override fun getAct() = this

    override fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?) {
        if (defaultAddressItem != null) {
            // 기본 배송지가 존재할 경우
            viewDataBinding.orderAddress.visibility = View.VISIBLE
            viewDataBinding.orderAddressOtherInfoContainer.visibility = View.VISIBLE
            viewDataBinding.orderAddressDefaultBadge.visibility = View.VISIBLE
            viewDataBinding.orderAddressEmptyText.visibility = View.GONE

            viewDataBinding.orderAddress.text = defaultAddressItem.address
            viewDataBinding.orderAddressPhone.text = defaultAddressItem.addressPhone
            viewDataBinding.orderAddressReceiver.text = defaultAddressItem.addressReceiver
        } else {
            // 기본 배송지가 존재하지 않을 경우
            viewDataBinding.orderAddress.visibility = View.GONE
            viewDataBinding.orderAddressOtherInfoContainer.visibility = View.GONE
            viewDataBinding.orderAddressDefaultBadge.visibility = View.GONE
            viewDataBinding.orderAddressEmptyText.visibility = View.VISIBLE
        }
    }
}