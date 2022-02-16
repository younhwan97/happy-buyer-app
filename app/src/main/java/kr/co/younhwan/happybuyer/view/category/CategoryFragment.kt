package kr.co.younhwan.happybuyer.view.category

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.younhwan.happybuyer.data.source.image.SampleImageRepository
import kr.co.younhwan.happybuyer.data.source.product.ProductRepository
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.view.category.adapter.CategoryAdapter
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryContract
import kr.co.younhwan.happybuyer.view.category.presenter.CategoryPresenter
import kr.co.younhwan.happybuyer.view.main.home.adapter.HomeAdapter
import kr.co.younhwan.happybuyer.view.main.home.presenter.HomePresenter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.thread


class CategoryFragment:Fragment(), CategoryContract.View {

    /* View Binding */
    private lateinit var viewDataBinding : FragmentCategoryBinding

    /* Presenter */
    private val categoryPresenter: CategoryPresenter by lazy {
        // View 영역은 사용자 이벤트 등에 대응하기 위해서 Presenter 변수가 필요하다.
        // 실제 처리는 Presenter, Model 에서 이뤄지기 때문이다.
        CategoryPresenter(
            this,
            productData = ProductRepository,
            adapterModel = categoryAdapter,
            adapterView = categoryAdapter
        )
    }

    /* Adapter */
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter()
    }

    /* Data */
    lateinit var selectedCateogory: String

    val productIdList = ArrayList<Int>()
    val productPriceList = ArrayList<Int>()
    val productNameList = ArrayList<String>()
    val productImageUriList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = FragmentCategoryBinding.inflate(inflater)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCateogory = arguments?.getString("category").toString()
        categoryPresenter.loadProductItems(requireContext(), false, selectedCateogory)

        viewDataBinding.itemContainer.run {
            this.adapter = categoryAdapter
            this.layoutManager = GridLayoutManager(activity as CategoryActivity, 2)
        }

        // getProductList(true)
    }

    // Recycler view
    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>(){
        // 항목 구성을 위한 사용할 ViewHolder 객체가 필요할 때 사용되는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val itemBinding = ItemBinding.inflate(layoutInflater)
            return ViewHolderClass(itemBinding)
        }

        override fun getItemCount(): Int {
            return productIdList.size
        }

        // ViewHolder 를 이용해 항목 내의 객체에 데이터를 셋팅
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            val metrics : DisplayMetrics = resources.displayMetrics
            val outSidePadding = Math.round(15 * metrics.density)
            val inSidePadding = Math.round(5 * metrics.density)
            val topPadding = Math.round(5 * metrics.density)

            if(position % 2 == 0)
                holder.itemContainer.setPadding(outSidePadding, topPadding, inSidePadding,0)
            else
                holder.itemContainer.setPadding(inSidePadding, topPadding, outSidePadding,0)

            val url = productImageUriList[position]

            holder.apply {
                Glide.with(holder.itemView.context).load(url).into(holder.itemImage)
                itemName.text = productNameList[position]
                itemPrice.text = DecimalFormat("#,###").format(productPriceList[position])
            }

            // Set click event listener
            holder.shoppingCartBtn.setOnClickListener {

            }

            holder.hearBtn.setOnClickListener {

            }

            holder.itemName.setOnClickListener {

            }

            holder.itemImage.setOnClickListener {

            }

            holder.itemPrice.setOnClickListener {

            }
        }
        
        // ViewHolder 클래스
        inner class ViewHolderClass(itemBinding: ItemBinding)
            : RecyclerView.ViewHolder(itemBinding.root) {
            val itemContainer = itemBinding.itemContainer
            val itemName = itemBinding.itemName
            val itemImage = itemBinding.itemImage
            val itemPrice = itemBinding.itemPrice
            val hearBtn = itemBinding.heartBtn
            val shoppingCartBtn = itemBinding.shoppingCartBtn
        }
    }

    private fun getProductList(clear:Boolean){
        if(clear){
            productNameList.clear()
            productImageUriList.clear()
        }

        thread {
            val selectedCategory = arguments?.getString("category")

            val client = OkHttpClient()
            val site = "http://192.168.0.11/products/api/app/read?category=${selectedCategory}"

            val request = Request.Builder().url(site).get().build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                val json = JSONObject(resultText)
                val data = JSONArray(json["data"].toString())

                for (i in 0 until data.length()){
                    val obj = data.getJSONObject(i)
                    val productStatus = obj.getString("status")
                    val productCategory = obj.getString("category")

                    if(productStatus == "판매중" && productCategory == selectedCategory){
                        productIdList.add(obj.getInt("product_id"))
                        productPriceList.add(obj.getInt("price"))
                        productNameList.add(obj.getString("name"))
                        productImageUriList.add(obj.getString("image_url"))
                    } else { // 판매중인 상품이 아니거나 잘못된 카테고리의 상품
                        continue
                    }
                }

                activity?.runOnUiThread{
                    viewDataBinding.itemContainer.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}
