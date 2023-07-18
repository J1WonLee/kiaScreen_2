package com.copy.kiascreen.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.copy.kiascreen.R

class BlankCarImageView @JvmOverloads constructor(context : Context, attr : AttributeSet? = null, def : Int = 0 ) : AppCompatImageView(context, attr, def) {
    var isTrimSelected = false
    init {
        setImageDrawable(resources.getDrawable(R.drawable.car_blank_))
    }

    fun changeToBlankImg() {
        if (isTrimSelected) {
            setImageDrawable(resources.getDrawable(R.drawable.car_blank_))
            isTrimSelected = false
        }
    }

    fun changeToCarImg(uri : String) {
        Glide.with(context).load(uri).into(this)
        isTrimSelected = true
    }

}