package kr.co.younhwan.happybuyer.view.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kr.co.younhwan.happybuyer.R
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.data.BasketItem
import kr.co.younhwan.happybuyer.data.source.address.AddressRepository
import kr.co.younhwan.happybuyer.databinding.ActivityOrderBinding
import kr.co.younhwan.happybuyer.view.addeditaddress.AddAddressActivity
import kr.co.younhwan.happybuyer.view.address.AddressActivity
import kr.co.younhwan.happybuyer.view.order.adapter.OrderAdapter

class OrderActivity : AppCompatActivity(), OrderContract.View {
    lateinit var viewDataBinding: ActivityOrderBinding

    private val orderPresenter: OrderPresenter by lazy {
        OrderPresenter(
            view = this,
            addressData = AddressRepository,
            orderAdapterModel = orderAdapter,
            orderAdapterView = orderAdapter
        )
    }

    private val orderAdapter: OrderAdapter by lazy {
        OrderAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        orderPresenter.loadDefaultAddress()
        if (intent.hasExtra("selected_basket_item")) {
            val selectedBasketItem =
                intent.getParcelableArrayListExtra<BasketItem>("selected_basket_item")

            orderPresenter.setOrderProduct(selectedBasketItem!!)
        } else {
            finish()
        }

        // 툴바
        viewDataBinding.orderToolbar.setNavigationOnClickListener {
            finish()
        }

        // 배달정보
        val startAddAddressActForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    orderPresenter.loadDefaultAddress()
                }
            }

        viewDataBinding.orderAddressAddBtn.setOnClickListener {
            val addAddressIntent = Intent(this, AddAddressActivity::class.java)
            startAddAddressActForResult.launch(addAddressIntent)
        }

        val startAddressActForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val snack = if (it.data?.hasExtra("address") == true) {
                        val newAddress = it.data?.getParcelableExtra<AddressItem>("address")

                        runOnUiThread {
                            viewDataBinding.orderAddressReceiver.text = newAddress?.addressReceiver
                            viewDataBinding.orderAddress.text = newAddress?.address
                            viewDataBinding.orderAddressPhone.text = newAddress?.addressPhone
                        }

                        Snackbar.make(viewDataBinding.root, "주소가 변경되었습니다.", Snackbar.LENGTH_SHORT)
                    } else {
                        orderPresenter.loadDefaultAddress()

                        Snackbar.make(
                            viewDataBinding.root,
                            "알 수 없는 에러로 주소가 변경되지 않았습니다. ",
                            Snackbar.LENGTH_SHORT
                        )
                    }

                    snack.show()
                }
            }

        viewDataBinding.orderAddressSelectBtn.setOnClickListener {
            val addressIntent = Intent(this, AddressActivity::class.java)
            addressIntent.putExtra("is_select_mode", true)
            startAddressActForResult.launch(addressIntent)
        }

        // 요청사항
        viewDataBinding.orderProductRecycler.adapter = orderAdapter
        viewDataBinding.orderProductRecycler.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }
        viewDataBinding.orderProductRecycler.addItemDecoration(orderAdapter.RecyclerDecoration())
    }

    override fun getAct() = this

    override fun loadDefaultAddressCallback(defaultAddressItem: AddressItem?) {
        if (defaultAddressItem != null) {
            // 기본 배송지가 존재할 경우
            viewDataBinding.orderAddressContent.visibility = View.VISIBLE
            viewDataBinding.orderAddressEmptyContent.visibility = View.GONE

            viewDataBinding.orderAddressReceiver.text = defaultAddressItem.addressReceiver
            viewDataBinding.orderAddress.text = defaultAddressItem.address
            viewDataBinding.orderAddressPhone.text = defaultAddressItem.addressPhone
        } else {
            // 기본 배송지가 존재하지 않을 경우
            viewDataBinding.orderAddressContent.visibility = View.GONE
            viewDataBinding.orderAddressEmptyContent.visibility = View.VISIBLE
        }
    }
}