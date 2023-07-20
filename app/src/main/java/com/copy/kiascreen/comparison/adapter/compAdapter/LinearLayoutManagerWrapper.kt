package com.copy.kiascreen.comparison.adapter.compAdapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

// customLinnearLayout으로, 아이템 초기화 후에 holderPosition을 못잡는 경우가 있어서 커스텀으로 만들어서 사용
class LinearLayoutManagerWrapper : LinearLayoutManager {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}