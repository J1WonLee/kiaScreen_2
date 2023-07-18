package com.copy.kiascreen.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.copy.kiascreen.R
import com.copy.kiascreen.custom.layout.LayoutMenuChild

class MenuToggleImageView @JvmOverloads constructor(context : Context, attr : AttributeSet? = null, def : Int = 0 ) : AppCompatImageView(context, attr, def) {
    var isExpanded = false
    init {
        setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
    }

    fun toggleEvent(child : LinearLayout) : Boolean {
        return if (isExpanded) {
            // 하위 메뉴 안보여줌
            child.visibility = GONE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
            this.isExpanded = !isExpanded
            false
        } else {
            // 하위 메뉴 보여줌
            child.visibility = VISIBLE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_less_24))
            this.isExpanded = !isExpanded
            true
        }
    }

    fun toggleCustomEvent(child : LayoutMenuChild) : Boolean {
        return if (isExpanded) {
            // 하위 메뉴 안보여줌
            child.visibility = GONE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
            this.isExpanded = !isExpanded
            false
        } else {
            // 하위 메뉴 보여줌
            child.visibility = VISIBLE
            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_less_24))
            this.isExpanded = !isExpanded
            true
        }
    }

    fun hideMenu(child : LinearLayout) {
        this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
        child.visibility = GONE
        isExpanded = false
    }

    fun hideCustomMenu(child : LayoutMenuChild) {
        this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
        child.visibility = GONE
        isExpanded = false
    }


}