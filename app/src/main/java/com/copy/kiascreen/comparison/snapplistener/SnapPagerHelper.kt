package com.copy.kiascreen.comparison.snapplistener

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class SnapPagerHelper : PagerSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        val firstVisiblePosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val firstItem = 0
        val lastItem = layoutManager.itemCount - 1

        return when {
            firstItem == firstVisiblePosition -> layoutManager.findViewByPosition(firstVisiblePosition)
            lastItem == lastVisiblePosition -> layoutManager.findViewByPosition(lastVisiblePosition)
            else -> super.findSnapView(layoutManager)
        }
    }


}