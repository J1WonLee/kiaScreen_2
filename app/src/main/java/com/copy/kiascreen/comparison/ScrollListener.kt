package com.copy.kiascreen.comparison

import android.util.Log
import android.widget.Scroller
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewpager2.widget.ViewPager2


// recyclerView scrollListener Factory class

class ScrollListener(context : ScrollListenerBridge) {
    private val mListener = context

    // 첫번째 리사이클러 뷰
    val oneListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                var firstVisibleItemPosition = ((recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                var lastVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                Log.d("scrollTest", "Idle firstPosition = $firstVisibleItemPosition")
                Log.d("scrollTest", "Idle lastVisiblePosition = $lastVisiblePosition")


                // 처음으로 포커싱 된 아이템의 포지션이 지정된 경우
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    // 다른 recyclerView 포지션 이동
                    mListener.moveViewPosition(firstVisibleItemPosition)
                }
                else if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                    mListener.moveViewPosition(lastVisiblePosition)
                }

            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)


        }
    }

    val stickyHeaderListener = object : RecyclerView.OnScrollListener() {
        var lastScrollPosition = 0
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                var firstVisibleItemPosition = ((recyclerView.layoutManager) as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                var lastVisiblePosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()

                Log.d("scrollTest", "Idle firstPosition = $firstVisibleItemPosition")
                Log.d("scrollTest", "Idle lastVisiblePosition = $lastVisiblePosition")

                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    // 다른 recyclerView 포지션 이동
                    lastScrollPosition = firstVisibleItemPosition
                    // vp2 포지션 이동
                    mListener.moveViewPosition(firstVisibleItemPosition)


                }
                else if (lastVisiblePosition != RecyclerView.NO_POSITION) {
                    // 다른 recyclerView 포지션 이동
                    lastScrollPosition = lastVisiblePosition


                    // vp2 포지션 이동
                    mListener.moveViewPosition(lastVisiblePosition)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    val pagerCallBack = object : ViewPager2.OnPageChangeCallback() {
        var curPage = 0
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            curPage = position
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)

            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                mListener.movePagerPosition(curPage)
            }
        }
    }

    // 성능 pager 만 페이지 이동을 시켜주는 리스너
    val pgaerCallBack2 = object : ViewPager2.OnPageChangeCallback() {
        var curPage = 0
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            curPage = position
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)

            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                mListener.movePerformPagerPosition(curPage)
            }
        }
    }

    interface ScrollListenerBridge {
        fun moveViewPosition(targetPosition : Int)

        fun movePagerPosition(targetPosition: Int)

        fun movePerformPagerPosition(targetPosition: Int)
    }
}
