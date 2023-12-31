package com.copy.kiascreen.comparison.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.vo.SelectedOption
import com.copy.kiascreen.databinding.ItemStickyItemBinding

class HeaderRecyclerAdapter(val context: Context) : RecyclerView.Adapter<HeaderRecyclerAdapter.HeaderHolder>() {
    private var selectedItem = mutableListOf<SelectedOption>()
    private var basePrice = 0
    private var mInterface : HeaderInterface? = null

    fun getItemSize() = selectedItem.size

    // stickyHeader이 그려지는건 항상 3개임, 선택된 아이템이 2개여도 아이템은 3개를 보여주고 마지막 아이템에 추가해달라는 것을 보여줘야 한다.
    fun setSelectedItem(itemList : MutableList<SelectedOption>) {
        this.selectedItem = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun addSelectedItem(item : SelectedOption) {
        try {
            Log.d("headerAdapterTest", "addSelectedItem  before add size = ${selectedItem.size} ====")
            this.selectedItem.add(item)
            Log.d("headerAdapterTest", "after add item size : ${selectedItem.size}")
            notifyItemInserted(selectedItem.size - 1)
        }catch (e : Exception) {
            Log.d("headerAdapterTest", "addSelectedItem  exception  = ${e.message} ====")
        }

    }

    fun deleteSelectedItem(position: Int) {
        Log.d("headerAdapterTest", "deleteSelectedItem  before add size = ${selectedItem.size} & position = $position")
        this.selectedItem.removeAt(position)
        Log.d("headerAdapterTest", "deleteSelectedItem  before add size = ${selectedItem.size}")
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, 1)
    }

    fun updateSelectedItem(item : SelectedOption, position: Int) {
        this.selectedItem[position] = item
        notifyItemChanged(position)
    }

    fun setInterface(instance : HeaderInterface) {
        this.mInterface = instance
    }

    inner class HeaderHolder(val binding : ItemStickyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SelectedOption, position: Int) {
            initData(binding, item, position)
            setModelChangeClick(binding)
        }

        fun bindLastItem() {
            initEmptyData(binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderHolder {
        val binding = ItemStickyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderHolder(binding)
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: HeaderHolder, position: Int) {
        if (selectedItem.size > holder.adapterPosition) {
            holder.bind(selectedItem[holder.adapterPosition], holder.adapterPosition)
        }
        else {
            // 선택된 항목들의 갯수가 3보다 적을경우, 마지막 항목에 추가해달라고 표시를 해줘야 함
            holder.bindLastItem()
        }

    }

    private fun initData(binding : ItemStickyItemBinding, item : SelectedOption , adapterPosition : Int) {
        Log.d("headerAdapterTest", "initData position = $adapterPosition")
        if (adapterPosition == 0) {
            // 첫번째 아이템인 경우 (비교 가격을 저장해 둬야 한다)
            basePrice = item.price
        }
        else {
            if (basePrice > item.price) {
                // 글자색 파란색, down
                binding.stickyItemComparePriceTv.text = (basePrice - item.price).toString() +" 원"
                binding.stickyItemComparePriceTv.setTextColor(context.resources.getColor(R.color.blue))
                binding.stickyItemUpDownImg.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }
            else {
                // 글자색 붉은색 up
                binding.stickyItemComparePriceTv.text = (basePrice - item.price).toString() +" 원"
                binding.stickyItemComparePriceTv.setTextColor(context.resources.getColor(R.color.red))
                binding.stickyItemUpDownImg.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }
        binding.stickyItemChangeBrandTv.visibility = View.VISIBLE
        binding.stickyPriceWrapper.visibility = View.VISIBLE
        binding.stickyItemBrandTv.text = item.brandName
        binding.stickyItemCarName.text = item.carName
        binding.stickyItemPriceTv.text = item.price.toString()

    }

    // 모델 변경 클릭시 , 모델 추가 클릭시 상단으로 이동
    fun setModelChangeClick(binding : ItemStickyItemBinding) {
        binding.stickyItemChangeBrandTv.setOnClickListener {
            mInterface?.scrollToPosition()
        }
    }

    fun initEmptyData(binding : ItemStickyItemBinding) {
        // 마지막 항목이 비어있는 경우 -> 추가하라는 뷰를 보여준다
        binding.stickyNoItemLinear.visibility = View.VISIBLE
        binding.stickyItemChangeBrandTv.visibility = View.GONE
        binding.stickyPriceWrapper.visibility = View.INVISIBLE

        binding.stickyNoItemLinear.setOnClickListener {
            mInterface?.scrollToPosition()
        }
    }
}

interface HeaderInterface {
    fun scrollToPosition()
}