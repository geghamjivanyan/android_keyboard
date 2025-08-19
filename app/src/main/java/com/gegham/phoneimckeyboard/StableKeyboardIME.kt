package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.graphics.Color

/**
 * Минимальная стабильная IME
 */
class StableKeyboardIME : InputMethodService() {
    
    override fun onCreateInputView(): View {
        // Самая простая реализация - одна кнопка
        return Button(this).apply {
            text = "СТАБИЛЬНАЯ КЛАВИАТУРА"
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.WHITE)
            
            setOnClickListener {
                currentInputConnection?.commitText("СТАБИЛЬНО", 1)
            }
        }
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        return false // Отключаем полноэкранный режим
    }
} 