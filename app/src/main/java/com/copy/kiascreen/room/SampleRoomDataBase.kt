package com.copy.kiascreen.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.copy.kiascreen.roomVo.User

@Database(
    entities =  [
        User::class
    ], version = 2
    , exportSchema = false
)

@TypeConverters(DateConverts::class)
abstract class SampleRoomDataBase : RoomDatabase() {
    abstract fun  roomDao() : RoomDao
}

