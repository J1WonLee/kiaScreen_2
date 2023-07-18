package com.copy.kiascreen.custom

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import com.copy.kiascreen.R

@RequiresApi(Build.VERSION_CODES.O)
class MenuItemText @JvmOverloads constructor(context : Context, attr : AttributeSet? = null, def : Int = 0) : androidx.appcompat.widget.AppCompatTextView(context, attr, def) {
    init {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        setPadding(resources.getDimension(R.dimen.menu_item_padding).toInt())
        typeface = resources.getFont(R.font.kia_light)
    }
}