package com.copy.kiascreen.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.copy.kiascreen.roomVo.User

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertUser(user : User) : Long

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUser(id : String) : User?

    @Query("Select * from User where id =:id and pwd =:pwd")
    suspend fun login(id:String, pwd:String) : User?

    @Query("Select * from User where id =:id")
    suspend fun autoLogin(id:String) : User?

    @Query("select * from User where mail =:mail")
    suspend fun findId(mail : String) : User?

    @Query("select * from User where id =:id and mail =:mail")
    suspend fun findPwd(id : String, mail : String) : User?

    @Query("UPDATE User SET pwd = :pwd where id = :id")
    suspend fun resetPwd(pwd : String, id : String) : Int

    @Update
    suspend fun updateUser(user:User) : Int

}