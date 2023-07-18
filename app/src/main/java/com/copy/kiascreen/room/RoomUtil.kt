package com.copy.kiascreen.room

sealed class RoomUtil<out T> {
    object Loading : RoomUtil<Nothing>()
    class Success<T> (val data : T?) : RoomUtil<T>()
    class Error(val error : Throwable?) : RoomUtil<Nothing>()
}

fun <T>RoomUtil<T>.successOrNull() : T? = if (this is RoomUtil.Success<T>) {
    data
} else {
    null
}
