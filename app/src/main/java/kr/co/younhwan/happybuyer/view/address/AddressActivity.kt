package kr.co.younhwan.happybuyer.view.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.databinding.ActivityAddressBinding
import kr.co.younhwan.happybuyer.view.addeditaddress.AddAddressActivity

class AddressActivity : AppCompatActivity(), AddressContract.View {
    lateinit var viewDataBinding: ActivityAddressBinding

    private val addressPresenter: AddressPresenter by lazy {
        AddressPresenter(
            view = this,
            addressData = AddressRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        addressPresenter.loadAddress()

        // 툴바
        viewDataBinding.addressToolbar.setNavigationOnClickListener {
            finish()
        }

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_CANCELED) {

                } else if (it.resultCode == RESULT_OK) {
                    val addressId = it.data?.getIntExtra("address_id", -1)
                    if (addressId != -1) {
                        val newAddressItem = AddressItem(
                            addressId = addressId!!,
                            addressReceiver = it.data?.getStringExtra("receiver"),
                            addressPhone = it.data?.getStringExtra("phone"),
                            address = it.data?.getStringExtra("address")
                        )


                    }
                }
            }
        viewDataBinding.addressToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addInAddress -> {
                    val addAddressIntent = Intent(this, AddAddressActivity::class.java)
                    startForResult.launch(addAddressIntent)
                    true
                }

                else -> false
            }
        }
    }

    override fun getAct() = this
}