package com.copy.kiascreen.roomVo

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Entity
@Parcelize
data class User (
    @PrimaryKey(autoGenerate = false)
    var id : String,
    var pwd : String,
    var name : String,
    var mail : String,
    var regitDate : Date = Date()

) : Parcelable {

    val createdDateFormat : String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            SimpleDateFormat("yyyy/MM/dd").apply {
                return this.format(regitDate)
            }
        }
}
