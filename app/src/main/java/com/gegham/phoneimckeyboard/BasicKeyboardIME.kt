package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.graphics.Color

/**
 * Базовая IME
 */
class BasicKeyboardIME : InputMethodService() {
    
    override fun onCreateInputView(): View {
        return Button(this).apply {
            text = "БАЗОВАЯ КЛАВИАТУРА"
            setBackgroundColor(Color.RED)
            setTextColor(Color.WHITE)
            
            setOnClickListener {
                currentInputConnection?.commitText("ТЕСТ", 1)
            }
        }
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        return false
    }
} 