package com.copy.kiascreen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copy.kiascreen.room.RoomRepository
import com.copy.kiascreen.room.RoomUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: RoomRepository) : ViewModel(){
    private val _logInSharedFlow = MutableSharedFlow<RoomUtil<*>>(replay = 1)
    var logInSharedFlow = _logInSharedFlow.asSharedFlow()

    private val _findIdSharedFlow = MutableSharedFlow<RoomUtil<*>>(replay = 1)
    var findIdSharedFlow = _findIdSharedFlow.asSharedFlow()

    private val _resetPwdSharedFlow = MutableSharedFlow<RoomUtil<*>>(replay = 1)
    var resetPwdSharedFlow = _resetPwdSharedFlow.asSharedFlow()

    fun login(id : String, pwd : String) {
        viewModelScope.launch {
            repository.login(id, pwd).collect {
                _logInSharedFlow.emit(it)
            }
        }
    }

    fun autoLogin(id : String) {
        viewModelScope.launch {
            repository.autoLogin(id).collect {
                _logInSharedFlow.emit(it)
            }
        }
    }

    fun findId(mail : String) {
        viewModelScope.launch {
            repository.findId(mail).collect {
                _findIdSharedFlow.emit(it)
            }
        }
    }

    fun findPwd(id : String, mail : String) {
        viewModelScope.launch {
            repository.resetPwd(id, mail).collect {
                _resetPwdSharedFlow.emit(it)
            }
        }
    }

}