package com.copy.kiascreen.room

import com.copy.kiascreen.roomVo.User
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun registerUser(user : User) : Flow<RoomUtil<Long>>

    fun chkDupId(id : String) : Flow<RoomUtil<User>>

    fun login(id: String, pwd : String) : Flow<RoomUtil<User>>

    fun autoLogin(id : String) : Flow<RoomUtil<User>>

    fun findId(mail : String) : Flow<RoomUtil<User>>

    fun resetPwd(id : String, mail:String) : Flow<RoomUtil<String>>

    fun updateUserData(user : User) : Flow<RoomUtil<Int>>
}