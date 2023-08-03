package com.copy.kiascreen.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import com.copy.kiascreen.R

class MenuWrapperLayout @JvmOverloads constructor(context: Context, attr : AttributeSet? = null, def : Int = 0) : ConstraintLayout(context, attr, def) {
    init {
        setBackgroundColor(resources.getColor(R.color.menu_wrapper))
        setPadding(resources.getDimension(R.dimen.menu_item_wrapper_padding).toInt())
    }

}