package kr.co.younhwan.happybuyer.view.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.databinding.ActivityAddressBinding
import kr.co.younhwan.happybuyer.view.addeditaddress.AddAddressActivity
import kr.co.younhwan.happybuyer.view.address.adapter.AddressAdapter

class AddressActivity : AppCompatActivity(), AddressContract.View {
    lateinit var viewDataBinding: ActivityAddressBinding

    private val addressPresenter: AddressPresenter by lazy {
        AddressPresenter(
            view = this,
            addressData = AddressRepository,
            addressAdapterModel = addressAdapter,
            addressAdapterView = addressAdapter
        )
    }

    private val addressAdapter: AddressAdapter by lazy {
        AddressAdapter()
    }

    private var isSelectMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        isSelectMode = intent.getBooleanExtra("is_select_mode",false)
        addressPresenter.loadAddress(isSelectMode)

        // 툴바
        viewDataBinding.addressToolbar.setNavigationOnClickListener {
            finish()
        }

        viewDataBinding.addressToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addInAddress -> {
                    val addAddressIntent = Intent(this, AddAddressActivity::class.java)
                    startActivity(addAddressIntent)
                    true
                }
                else -> false
            }
        }

        // 리사이클러뷰
        viewDataBinding.addressRecycler.adapter = addressAdapter
        viewDataBinding.addressRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.addressRecycler.addItemDecoration(addressAdapter.RecyclerDecoration())
    }

    override fun onResume() {
        super.onResume()
        addressPresenter.loadAddress(isSelectMode)
    }

    override fun getAct() = this

    override fun loadAddressCallback(addressItemCount: Int) {

    }

    override fun createAddAddressAct(addressItem: AddressItem) {
        val addAddressIntent = Intent(this, AddAddressActivity::class.java)
        addAddressIntent.putExtra("address", addressItem)
        startActivity(addAddressIntent)
    }
}