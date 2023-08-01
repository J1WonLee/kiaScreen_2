package com.copy.kiascreen.mypage.samplevo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnapItem(val imgDrawable : Int, val title : String, val content : String) : Parcelable

