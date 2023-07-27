package com.copy.kiascreen.util

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

class KeyboardScrollUtil (
    private val window : Window,
    private val onShowKeyboard : ((keyboardHeight : Int) -> Unit)? = null,
    private val onHideKeyboard : (() -> Unit)? = null
) {
    private val MIN_KEYBOARD_HEIGHT = 150
    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight = 0

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        // 높이가 변할때 키보드가 보여지는지를 결정한다
        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT) {

                // 현재 키보드 높이 계산
                val currentKeyboardHeight = window.decorView.height - windowVisibleDisplayFrame.bottom

                // 키보드가 보여졌음을 리스너에게 알린다
                onShowKeyboard?.invoke(currentKeyboardHeight)
            }
            else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT < visibleDecorViewHeight) {

                // 키보드가 숨겨졌음을 리스너에게 알린다
                onHideKeyboard?.invoke()
            }
        }
        // 현재 화면의 높이를 저장한다.
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListener() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

}