package com.copy.kiascreen.custom

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderRvDecoration : RecyclerView.ItemDecoration() {

    private val mPaint = Paint()
    private val mStrokePaint = Paint()
    private val dp = Resources.getSystem().displayMetrics.density
    private val mInterpolator = AccelerateDecelerateInterpolator()

    private val mIndicatorStrokeWidth = dp * 0.5f
    private val mIndicatorHeight = (dp * 32).toInt()

    private val mIndicatorItemLength = dp * 16
    private val mIndicatorItemPadding = dp * 12

    init {
        mPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        mStrokePaint.apply {
            strokeWidth = mIndicatorStrokeWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = COLOR_ACTIVE.toInt()
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        var itemCount = (parent.adapter?.itemCount)!!

        var totalLength = mIndicatorItemLength * itemCount!!
        var paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
        var indicatorTotalWidth = totalLength + paddingBetweenItems
        var indicatorStartX = (parent.width - indicatorTotalWidth) / 2F

        var indicatorPosY = parent.height  - mIndicatorHeight  / 2.5F

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);

        val layoutManager =  parent.layoutManager as LinearLayoutManager
        val activePosition = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild!!.width

        val progress = mInterpolator.getInterpolation(left * -1 / width.toFloat())

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        mPaint.color = COLOR_INACTIVE.toInt()

        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

        var start = indicatorStartX
        for (i in 0 until itemCount) {
            c.drawCircle(start, indicatorPosY, mIndicatorItemLength / 2F, mPaint)
            c.drawCircle(start, indicatorPosY, mIndicatorItemLength / 2F, mStrokePaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, highlightPosition: Int, progress: Float, itemCount: Int) {
        mPaint.color = COLOR_ACTIVE.toInt()

        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

        if (progress === 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + itemWidth * highlightPosition
            c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemLength / 2f, mPaint)
            c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemLength / 2F, mStrokePaint)
        }
        else {
            val highlightStart = indicatorStartX + itemWidth * highlightPosition

            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress + mIndicatorItemPadding * progress
            c.drawCircle(highlightStart + partialLength, indicatorPosY, mIndicatorItemLength / 2f, mPaint)
            c.drawCircle(highlightStart + partialLength, indicatorPosY, mIndicatorItemLength / 2F, mStrokePaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = mIndicatorHeight
    }
}