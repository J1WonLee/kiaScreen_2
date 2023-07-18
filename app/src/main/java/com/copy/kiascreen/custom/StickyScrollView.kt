package com.copy.kiascreen.custom

import android.content.Context
import android.graphics.Interpolator
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.copy.kiascreen.showAnim

class StickyScrollView : NestedScrollView, ViewTreeObserver.OnGlobalLayoutListener{
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var isAnimated = false

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false

    private var mHeaderInitPosition = 0f

    var isModelCompareClicked = false

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t

        if (scrollY > mHeaderInitPosition && isModelCompareClicked) {
            stickHeader(scrolly - mHeaderInitPosition)
        }
        else {
            freeHeader()
        }


    }

    private fun stickHeader(position: Float) {
        header?.translationY = position
        header?.alpha = 1.0f
        Log.d("stickyHeaderTest", "start stickyheader position = $position")
//        if(!isAnimated) {
//            header?.animate()?.translationY(position)?.let {
//                it.interpolator = DecelerateInterpolator()
//                it.duration = 400
//            }
//            Log.d("stickyHeaderTest", "animation position = $position")
//            header?.animate()?.alpha(1.0f)
//            isAnimated = true
//        }
//        else {
//            header?.let{
//                Log.d("stickyHeaderTest", "non position = $position")
//                it.translationY = position
//            }
//        }
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    private fun freeHeader() {
        header?.translationY = 0f
        header?.alpha = 0f
        isAnimated = false
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}