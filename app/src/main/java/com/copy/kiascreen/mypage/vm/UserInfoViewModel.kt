package com.copy.kiascreen.mypage.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copy.kiascreen.room.RoomRepository
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.roomVo.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val repository: RoomRepository) : ViewModel() {
    private val _userInfoSharedFlow = MutableSharedFlow<RoomUtil<*>>()
    var userInfoSharedFlow = _userInfoSharedFlow.asSharedFlow()

    fun updateUser(user : User) {
        viewModelScope.launch {
            repository.updateUserData(user).collect {
                _userInfoSharedFlow.emit(it)
            }
        }
    }
}