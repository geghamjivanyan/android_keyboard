package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.graphics.Color
import android.util.Log

/**
 * Простая синяя клавиатура с одной кнопкой
 */
class BlueIME : InputMethodService() {
    
    private val TAG = "BlueIME"
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "BlueIME создана")
    }
    
    override fun onCreateInputView(): View {
        Log.d(TAG, "BlueIME: onCreateInputView вызван")
        
        // Создаем простую кнопку
        val button = Button(this)
        button.text = "СИНЯЯ КНОПКА"
        button.setBackgroundColor(Color.BLUE)
        button.setTextColor(Color.WHITE)
        button.textSize = 20f
        
        // Обработка нажатия
        button.setOnClickListener {
            // Вставляем текст при нажатии
            currentInputConnection?.commitText("BLUE", 1)
        }
        
        return button
    }
    
    override fun onStartInputView(info: android.view.inputmethod.EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "BlueIME: onStartInputView вызван")
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        // Отключаем полноэкранный режим
        return false
    }
} 