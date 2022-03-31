package kr.co.younhwan.happybuyer.view.address

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onResume() {
        super.onResume()
        addressPresenter.loadAddress(isSelectMode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        // 주소록 모드를 설정
        // 주소록 엑티비티가 주문 엑티비티에서 호출된 경우  -> select mode
        // 주소록 엑티비티가 계정 프래그먼트에서 호출된 경우 -> not select mode
        isSelectMode = if (intent.hasExtra("is_select_mode")) {
            intent.getBooleanExtra("is_select_mode", false)
        } else {
            false
        }

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

        // 배송지 리사이클러뷰
        viewDataBinding.addressRecycler.adapter = addressAdapter
        viewDataBinding.addressRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.addressRecycler.addItemDecoration(addressAdapter.RecyclerDecoration())
    }

    override fun getAct() = this

    override fun loadAddressCallback(resultCount: Int) {
        if (resultCount == 0) {
            // 주소가 존재하지 않을 때 (-> Empty view 를 표시)
            viewDataBinding.addressEmptyView.visibility = View.VISIBLE
            viewDataBinding.addressRecycler.visibility = View.GONE
        } else {
            // 주소가 존재할 때
            viewDataBinding.addressEmptyView.visibility = View.GONE
            viewDataBinding.addressRecycler.visibility = View.VISIBLE
        }
    }

    override fun createAddAddressAct(addressItem: AddressItem) {
        val addAddressIntent = Intent(this, AddAddressActivity::class.java)
        addAddressIntent.putExtra("address", addressItem)
        startActivity(addAddressIntent)
    }

    override fun finishAct(addressItem: AddressItem) {
        val resultIntent = Intent()
        resultIntent.putExtra("address", addressItem)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}