package com.copy.kiascreen.mypage.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.copy.kiascreen.mypage.fragment.SnapPagerFragment
import com.copy.kiascreen.mypage.samplevo.SnapItem

class SnapPagerAdapter(activity : FragmentActivity, itemList : List<SnapItem>) : FragmentStateAdapter(activity) {
    private val itemMap = HashMap<Int, SnapPagerFragment>()
    init {
        try {
            for ((index, item) in itemList.withIndex()) {
                itemMap[index] = SnapPagerFragment.newInstance(item)
            }
        } catch (e : Exception) {
            Log.d("snapPager", "init error ${e.message}")
        }
    }

    override fun getItemCount() = itemMap.size

    override fun createFragment(position: Int): Fragment {
        return try {
            itemMap[position]!!
        } catch (e : java.lang.Exception) {
            Log.d("snapPager", "createFragment error ${e.message}")
            error("no Such Fragment")
        }
    }
}