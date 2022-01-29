package kr.co.younhwan.happybuyer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.databinding.FragmentCategoryBinding
import kr.co.younhwan.happybuyer.databinding.FragmentHomeBinding
import kr.co.younhwan.happybuyer.databinding.ItemBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class CategoryFragment:Fragment() {
    // View Binding
    lateinit var categoryFragmentBinding : FragmentCategoryBinding

    var imgRes = arrayOf(
        R.drawable.apple, R.drawable.apple,
    )

    var testName = arrayOf(
       "사과", "바나나"
    )
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        categoryFragmentBinding = FragmentCategoryBinding.inflate(inflater)
        return categoryFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = RecyclerAdapter()
        categoryFragmentBinding.itemContainer.adapter = adapter1

        categoryFragmentBinding.itemContainer.layoutManager = GridLayoutManager(activity as CategoryActivity, 2)

        /* server */
//        thread {
//            val site = "http://192.168.0.5:3000"
//            val client = OkHttpClient()
//
//            val request = Request.Builder().url(site).get().build()
//            val response = client.newCall(request).execute()
//
//            if(response.isSuccessful){
//                val result = response.body?.string()
//                Log.d("server","$result")
//            }else{
//                Log.d("server","실패")
//            }
//        }
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
            holder.itemImage.setImageResource(imgRes[position])
            holder.itemName.text = testName[position]
        }

        override fun getItemCount(): Int {
            return testName.size
        }

        // ViewHolder 클래스
        inner class ViewHolderClass(itemBinding: ItemBinding): RecyclerView.ViewHolder(itemBinding.root){
            val itemName = itemBinding.itemName
            val itemImage = itemBinding.itemImage
        }
    }

}