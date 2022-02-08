package kr.co.younhwan.happybuyer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class CategoryFragment:Fragment() {
    // View Binding
    private lateinit var categoryFragmentBinding : FragmentCategoryBinding

    val productIdList = ArrayList<Int>()
    val productStatusList = ArrayList<String>()
    val productCategoryList = ArrayList<String>()
    val productPriceList = ArrayList<Int>()
    val productNameList = ArrayList<String>()
    val productImageUriList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        categoryFragmentBinding = FragmentCategoryBinding.inflate(inflater)

        val adapter1 = RecyclerAdapter()
        categoryFragmentBinding.itemContainer.adapter = adapter1
        categoryFragmentBinding.itemContainer.layoutManager = GridLayoutManager(activity as CategoryActivity, 3)
        getProductList(true)

        return categoryFragmentBinding.root
    }

    // Recycler view
    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolderClass>(){
        // 항목 구성을 위한 사용할 ViewHolder 객체가 필요할 때 사용하는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val itemBinding = ItemBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(itemBinding)
            return holder
        }

        // ViewHolder를 통해 항목을 구성할 때 항목 내의 View 객체에 데이터를 세팅한다.
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.itemImage.setImageURI(Uri.parse(productImageUriList[position]))
            holder.itemName.text = productNameList[position]
            holder.itemPrice.text = DecimalFormat("#,###").format(productPriceList[position])
        }

        override fun getItemCount(): Int {
            return productNameList.size
        }

        // ViewHolder 클래스
        inner class ViewHolderClass(itemBinding: ItemBinding): RecyclerView.ViewHolder(itemBinding.root){
            val itemName = itemBinding.itemName
            val itemImage = itemBinding.itemImage
            val itemPrice = itemBinding.itemPrice
        }
    }

    private fun getProductList(clear:Boolean){
        if(clear){
            productNameList.clear()
            productImageUriList.clear()
        }

        thread {
            val categoryName = arguments?.getString("category")

            val client = OkHttpClient()
            val site = "http://192.168.0.5/products/api/app/read?category=${categoryName}"

            val request = Request.Builder().url(site).get().build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                val json = JSONObject(resultText)
                val data = JSONArray(json["data"].toString())

                for (i in 0 until data.length()){
                    val obj = data.getJSONObject(i)
                    productIdList.add(obj.getInt("product_id"))
                    productStatusList.add(obj.getString("status"))
                    productCategoryList.add(obj.getString("category"))
                    productPriceList.add(obj.getInt("price"))
                    productNameList.add(obj.getString("name"))
                    productImageUriList.add(obj.getString("image_url"))
                }

                activity?.runOnUiThread{
                    categoryFragmentBinding.itemContainer.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}
