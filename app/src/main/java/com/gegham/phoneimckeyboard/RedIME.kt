package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.graphics.Color
import android.util.Log

/**
 * Простая красная клавиатура с одной кнопкой
 */
class RedIME : InputMethodService() {
    
    private val TAG = "RedIME"
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "RedIME создана")
    }
    
    override fun onCreateInputView(): View {
        Log.d(TAG, "RedIME: onCreateInputView вызван")
        
        // Создаем простую кнопку
        val button = Button(this)
        button.text = "КРАСНАЯ КНОПКА"
        button.setBackgroundColor(Color.RED)
        button.setTextColor(Color.WHITE)
        button.textSize = 20f
        
        // Обработка нажатия
        button.setOnClickListener {
            // Вставляем текст при нажатии
            currentInputConnection?.commitText("RED", 1)
        }
        
        return button
    }
    
    override fun onStartInputView(info: android.view.inputmethod.EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "RedIME: onStartInputView вызван")
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        // Отключаем полноэкранный режим
        return false
    }
} 