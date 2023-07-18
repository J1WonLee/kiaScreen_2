package com.copy.kiascreen.room

import android.util.Log
import com.copy.kiascreen.roomVo.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RoomRepositoryImpl @Inject constructor(private  val roomDao : RoomDao): RoomRepository {
    override fun registerUser(user: User): Flow<RoomUtil<Long>> = flow<RoomUtil<Long>> {
        val response = roomDao.insertUser(user)
        Log.d("regitTest", "response = $response")
        if (response > 0) {
            emit(RoomUtil.Success(response))
        }
        else {
            throw java.lang.Exception()
        }
    }.catch { emit(RoomUtil.Error(it)) }

    override fun chkDupId(id: String): Flow<RoomUtil<User>>  = flow<RoomUtil<User>> {
        val response = roomDao.getUser(id)

        if (response != null) {
            emit(RoomUtil.Success(response))
        }
        else {
            emit(RoomUtil.Success(null))
        }
    }.catch { emit(RoomUtil.Error(it)) }

    override fun login(id: String, pwd: String): Flow<RoomUtil<User>> = flow<RoomUtil<User>>{
        val response = roomDao.login(id, pwd)

        emit(RoomUtil.Success(response))
    }.catch { emit(RoomUtil.Error(it)) }

    override fun findId(mail: String): Flow<RoomUtil<User>> = flow<RoomUtil<User>> {
        val response = roomDao.findId(mail)

        emit(RoomUtil.Success(response))
    }.catch { emit(RoomUtil.Error(it)) }

    override fun resetPwd(id: String, mail: String): Flow<RoomUtil<String>> = flow<RoomUtil<String>> {
        // 아이디 조회한다
        // 있으면 임의의 문자열 만들어서 리턴 없으면 null

        val userResponse = roomDao.findPwd(id, mail)

        if (userResponse == null) {
            emit(RoomUtil.Success(null))
            Log.d("updateTest", "response = null")
        }
        else {
            // 비밀번호 만들어서 보내준다.

            val charset = ('0' .. '9') + ('a' .. 'z')
            val rangeRandom = kotlin.collections.List(10) {charset.random()}.joinToString("")
            val response = roomDao.resetPwd(rangeRandom, id)

            Log.d("updateTest", "response = $response")

            emit(RoomUtil.Success(rangeRandom))
        }

    }.catch { emit(RoomUtil.Error(it)) }

    override fun updateUserData(user: User): Flow<RoomUtil<Int>> = flow<RoomUtil<Int>> {
        // 업데이트 결과 노출
        val response = roomDao.updateUser(user)

        // 리턴값은 영햐받은 행. 1개 아니라면 문제 있는거임.
        if (response == 1) {
            emit(RoomUtil.Success(response))
        }
        else {
            emit(RoomUtil.Success(-1))
        }
    }.catch { emit(RoomUtil.Error(it)) }


    override fun autoLogin(id: String): Flow<RoomUtil<User>> = flow<RoomUtil<User>> {
    val response = roomDao.autoLogin(id)

        emit(RoomUtil.Success(response))
    }.catch { emit(RoomUtil.Error(it)) }
}