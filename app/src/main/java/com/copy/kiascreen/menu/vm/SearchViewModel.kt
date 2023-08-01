package com.copy.kiascreen.menu.vm

import android.app.appsearch.SearchResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copy.kiascreen.menu.vo.MenuSearchItem
import com.copy.kiascreen.room.RoomRepository
import com.copy.kiascreen.room.RoomUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: RoomRepository) : ViewModel() {
    private val _searchSharedFlow = MutableSharedFlow<List<MenuSearchItem.SearchItem>>(replay = 0)
    var searchSharedFlow = _searchSharedFlow.asSharedFlow()

    fun getSearchResults() {
        viewModelScope.launch {
            _searchSharedFlow.emit(
                arrayListOf(
                    MenuSearchItem.SearchItem("니로2", true),
                    MenuSearchItem.SearchItem("니로 EV2", true),
                    MenuSearchItem.SearchItem("니로 플러스2", false)
                )
            )
        }
    }
}