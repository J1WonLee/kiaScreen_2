package com.copy.kiascreen.registry

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
class RegisterViewModel @Inject constructor(private val roomRepository  : RoomRepository) : ViewModel() {
    private val _userSharedFlow = MutableSharedFlow<RoomUtil<*>>(replay = 0)
    var userSharedFlow = _userSharedFlow.asSharedFlow()

    private val _regitSharedFlow = MutableSharedFlow<RoomUtil<*>>(replay = 0)
    var regitSharedFlow = _regitSharedFlow.asSharedFlow()

    fun regitMember(user : User) {
        viewModelScope.launch {
            roomRepository.registerUser(user).collect {
                _regitSharedFlow.emit(it)
            }
        }
    }

    fun chkDupId(id : String) {
        viewModelScope.launch {
            roomRepository.chkDupId(id).collect() {
                _userSharedFlow.emit(it)
            }
        }
    }
}