package com.copy.kiascreen.menu.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.databinding.ItemPopListBinding
import com.copy.kiascreen.databinding.ItemRecListBinding
import com.copy.kiascreen.databinding.ItemSearchResultBinding
import com.copy.kiascreen.menu.vo.MenuSearchItem


const val VIEW_TYPE_REC = 1
const val VIEW_TYPE_POP = 2
const val VIEW_TYPE_RESULT = 3
class SearchAdapter(itemList : List<MenuSearchItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = listOf<MenuSearchItem>()
    init {
        data = itemList
    }

    internal inner class ItemRecHolder(val binding : ItemRecListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : MenuSearchItem.RecItem) {
            binding.recResultTitle.text = item.title
        }
    }

    internal inner class ItemPopHolder(val binding : ItemPopListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : MenuSearchItem.PopItem) {
            binding.searchPopTitle.text = item.title
        }
    }

    internal inner class ItemResultHolder(val binding : ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : MenuSearchItem.SearchItem) {
            binding.searchResultText.text = item.title

            if (item.isShortCut) {
                binding.shortcutImg.visibility = View.VISIBLE
            }
            else {
                binding.shortcutImg.visibility = View.GONE
            }
        }

        fun itemClick(item : MenuSearchItem.SearchItem, position: Int) {
            // 해당 검색어 클릭시 이동
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position] is MenuSearchItem.SearchItem) {
            return VIEW_TYPE_RESULT
        }
        else if (data[position] is MenuSearchItem.RecItem) {
            return VIEW_TYPE_REC
        }
        return VIEW_TYPE_POP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_RESULT) {
            val searchBinding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemResultHolder(searchBinding)
        }

        if (viewType == VIEW_TYPE_REC) {
            val recBinding = ItemRecListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemRecHolder(recBinding)
        }

        val popBinding = ItemPopListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPopHolder(popBinding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]

        if (holder is ItemRecHolder && item is MenuSearchItem.RecItem) {
            holder.bind(data[position] as MenuSearchItem.RecItem)
        }

        if (holder is ItemPopHolder && item is MenuSearchItem.PopItem) {
            holder.bind(data[position] as MenuSearchItem.PopItem)
        }

        if (holder is ItemResultHolder && item is MenuSearchItem.SearchItem) {
            holder.bind(data[position] as MenuSearchItem.SearchItem)
        }
    }
}