package com.copy.kiascreen.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.databinding.ItemMypageRvBinding
import com.copy.kiascreen.mypage.MyPageAdapterInterface
import com.copy.kiascreen.mypage.samplevo.QuickMenu

// 마이페이지 상단에 보이는, rv adapter , 각 항목 클릭시 해당 페이지로 이동
class MyPageAdapter(val list : List<QuickMenu>) : RecyclerView.Adapter<MyPageAdapter.MyPageHolder>() {
    var menuList = mutableListOf<QuickMenu>()
    var mInterface : MyPageAdapterInterface? = null

    init {
        this.menuList = list.toMutableList()
    }

    fun setInterface(myPageAdapterInterface : MyPageAdapterInterface) {
        this.mInterface = myPageAdapterInterface
    }


    inner class MyPageHolder (val binding : ItemMypageRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item : QuickMenu) {
            binding.mypageRvCntTv.text = item.cnt
            binding.mypageRvContentTv.text = item.name
        }

        fun setClick(position: Int) {
            binding.mypageRvWrapper.setOnClickListener {
                mInterface?.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageHolder {
        val binding = ItemMypageRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageHolder(binding)
    }

    override fun getItemCount(): Int = this.menuList.size

    override fun onBindViewHolder(holder: MyPageHolder, position: Int) {
        holder.bind(menuList[position])
        holder.setClick(position)
    }
}