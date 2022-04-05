package kr.co.younhwan.happybuyer.view.address.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.younhwan.happybuyer.data.AddressItem
import kr.co.younhwan.happybuyer.databinding.RecyclerAddressItemBinding
import kr.co.younhwan.happybuyer.view.address.adapter.contract.AddressAdapterContract

class AddressAdapter :
    RecyclerView.Adapter<AddressViewHolder>(),
    AddressAdapterContract.Model,
    AddressAdapterContract.View {

    // 아이템 
    private var addressItemList: ArrayList<AddressItem> = ArrayList()
    private var isSelectMode: Boolean = false

    // 이벤트 리스너
    override var onClickFunOfEditBtn: ((AddressItem) -> Unit)? = null
    override var onClickFunOfSelectBtn: ((AddressItem) -> Unit)? = null
    
    // 메서드
    override fun getItemCount() = addressItemList.size

    override fun notifyAdapter() = notifyDataSetChanged()

    override fun addItems(addressItems: ArrayList<AddressItem>) {
        addressItemList = addressItems
    }

    override fun setSelectMode(canBeSelected: Boolean) {
        isSelectMode = canBeSelected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemBinding = RecyclerAddressItemBinding.inflate(LayoutInflater.from(parent.context))
        return AddressViewHolder(
            parent = parent,
            addressItemBinding = itemBinding,
            listenerFunOfEditBtn = onClickFunOfEditBtn,
            listenerFunOfSelectBtn = onClickFunOfSelectBtn
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        addressItemList[position].let {
            if(position == addressItemList.size - 1){ // 마지막 아이템
                holder.onBindLast(it, isSelectMode)
            } else {
                holder.onBind(it, isSelectMode)
            }
        }
    }

    // 데코레이션
    inner class RecyclerDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemPosition = parent.getChildAdapterPosition(view)
            val density = parent.resources.displayMetrics.density

            if(itemPosition == 0){
                outRect.top = (16 * density).toInt()
            } else if(itemPosition == addressItemList.size - 1){
                outRect.bottom = (16 * density).toInt()
            }
        }
    }
}