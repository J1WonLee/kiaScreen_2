package com.copy.kiascreen.menu.vo

sealed class MenuSearchItem {
    class RecItem(val title : String) : MenuSearchItem()
    class PopItem(val title : String) : MenuSearchItem()
    class SearchItem(val title : String, val isShortCut : Boolean = false) : MenuSearchItem()
}
