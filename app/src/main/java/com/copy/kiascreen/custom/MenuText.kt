package com.copy.kiascreen.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.copy.kiascreen.R


@RequiresApi(Build.VERSION_CODES.O)
class MenuText @JvmOverloads constructor(private val context : Context, attr : AttributeSet? = null, def : Int = 0)  : AppCompatTextView(context, attr, def) {
    init {
//        textSize = resources.getDimension(R.dimen.menu_text_size)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 33F)
        typeface = ResourcesCompat.getFont(context, R.font.kiabold)
    }
}