package com.copy.kiascreen.comparison.adapter

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.ItemCompRecCarBinding

class ComparRecImgAdapter(val context : Context) : RecyclerView.Adapter<ComparRecImgAdapter.CompRecViewHolder>() {
    var imgList = mutableListOf<String>(
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1189.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1189.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_52.png"
    )

    var mInterface : RecImgInterface? = null

    fun setLinks(lists : MutableList<String>) {
        this.imgList = lists
    }

    inner class CompRecViewHolder(val binding : ItemCompRecCarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Log.d("comeRecVpTest", "onBind Called!")
            Glide.with(context).load(imgList[position]).into(binding.recImg)
        }

        fun imgClick(position: Int) {
            binding.recImg.setOnClickListener {
                mInterface?.let {
                    it.onImgClick(1300, 5000)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompRecViewHolder {
        val binding = ItemCompRecCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompRecViewHolder(binding)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: CompRecViewHolder, position: Int) {
        holder.bind(position)
        holder.imgClick(position)
    }
}

interface RecImgInterface {
    fun onImgClick(basePrice : Int, totalPrice : Int)
}