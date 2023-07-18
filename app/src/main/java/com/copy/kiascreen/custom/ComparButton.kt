package com.copy.kiascreen.custom

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import com.copy.kiascreen.R

class ComparButton  @JvmOverloads constructor(context : Context, attr : AttributeSet? = null) : androidx.appcompat.widget.AppCompatButton(context, attr) {
    private var isChanged = false


    fun blockClick(btn : ComparButton ) {
        if (isChanged) {
            isChanged = false

            this.setBackgroundColor(context.resources.getColor(R.color.white))
            this.setTextColor(context.resources.getColor(R.color.border_color))

            btn.setBackgroundColor(context.resources.getColor(R.color.btn_gray))

            this.isClickable = false
            btn.isClickable = false
        }
    }

    fun enableClick(btn : ComparButton) {
        if (!isChanged) {
            isChanged = true

            this.setTextColor(context.resources.getColor(R.color.black))
            btn.setBackgroundColor(context.resources.getColor(R.color.black))

            this.isClickable = true
            btn.isClickable = true
        }
    }

}