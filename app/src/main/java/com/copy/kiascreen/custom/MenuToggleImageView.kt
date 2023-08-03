package com.copy.kiascreen.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.copy.kiascreen.R
import com.copy.kiascreen.custom.layout.LayoutMenuChild

class MenuToggleImageView @JvmOverloads constructor(context : Context, attr : AttributeSet? = null, def : Int = 0 ) : AppCompatImageView(context, attr, def) {
    var isExpanded = false
    private var expandAvd : AnimatedVectorDrawableCompat?
    private var closeAvd : AnimatedVectorDrawableCompat?
    init {
        setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
        expandAvd = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_drawer_expand)
        closeAvd = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_drawer_shut)
    }

    fun toggleEvent(child : LinearLayout) : Boolean {
//        this.animate().rotationBy(180f).setDuration(300).start()
        return if (isExpanded) {
            // 하위 메뉴 안보여줌
            child.visibility = GONE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
            this.setImageDrawable(expandAvd)
            expandAvd?.start()
            this.isExpanded = !isExpanded
            false
        } else {
            // 하위 메뉴 보여줌
            child.visibility = VISIBLE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_less_24))
            this.setImageDrawable(closeAvd)
            closeAvd?.start()
            this.isExpanded = !isExpanded
            true
        }
    }

    fun toggleWrapper(child : ConstraintLayout) : Boolean {
        Log.d("menuTest", "toggleWrapperEvent called")
//        this.animate().rotationBy(180f).setDuration(300).start()
        return if (isExpanded) {
            // 하위 메뉴 안보여줌
            child.visibility = GONE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
            this.setImageDrawable(expandAvd)
            expandAvd?.start()
            this.isExpanded = !isExpanded
            false
        } else {
            // 하위 메뉴 보여줌
            child.visibility = VISIBLE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_less_24))
            this.setImageDrawable(closeAvd)
            closeAvd?.start()
            this.isExpanded = !isExpanded
            true
        }
    }

    fun toggleCustomEvent(child : LayoutMenuChild) : Boolean {
        Log.d("menuTest", "toggleCustomEvent called")
//        this.animate().rotationBy(180f).setDuration(300).start()
        return if (isExpanded) {
            // 하위 메뉴 안보여줌
            child.visibility = GONE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
            this.setImageDrawable(expandAvd)
            expandAvd?.start()
            this.isExpanded = !isExpanded
            false
        } else {
            // 하위 메뉴 보여줌
            child.visibility = VISIBLE
//            this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_less_24))
            this.setImageDrawable(closeAvd)
            closeAvd?.start()
            this.isExpanded = !isExpanded
            true
        }
    }

    fun hideMenu(child : View) {
        this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))

        child.visibility = GONE
        isExpanded = false
    }

    fun hideMenuWrapper(child : MenuWrapperLayout) {
        this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
//        this.animate().rotationBy(180f).setDuration(300).start()
        child.visibility = GONE
        isExpanded = false
    }

    fun hideCustomMenu(child : LayoutMenuChild) {
        Log.d("menuTest", "hideCustomMenuCalled")
        this.setImageDrawable(resources.getDrawable(R.drawable.baseline_expand_more_24))
        child.visibility = GONE
        isExpanded = false
    }
}