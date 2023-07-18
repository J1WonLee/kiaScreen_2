package com.copy.kiascreen.retrofit

import com.copy.kiascreen.room.RoomUtil


sealed class RetrofitUtil<out T> {
    object Loading : RetrofitUtil<Nothing>()
    class Success<T>(val data : T?) : RetrofitUtil<T>()
    class Error(val error : Throwable) : RetrofitUtil<Nothing>()
}

fun <T> RetrofitUtil<T>.successOrNull() : T? = if (this is RetrofitUtil.Success<T>) {
    data
} else {
    null
}