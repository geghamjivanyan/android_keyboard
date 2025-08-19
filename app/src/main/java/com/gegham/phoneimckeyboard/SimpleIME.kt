package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.widget.Button
import android.view.Gravity
import android.widget.LinearLayout
import android.app.Dialog
import android.inputmethodservice.InputMethodService.Insets

class SimpleIME : InputMethodService() {
    
    private val TAG = "SimpleIME"
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SimpleIME создан")
    }
    
    override fun onCreateInputView(): View {
        Log.d(TAG, "SimpleIME: создаем вид клавиатуры")
        
        // Создаем большую кнопку, чтобы легко было нажать
        val button = Button(this)
        button.text = "КНОПКА"
        button.textSize = 30f
        
        // При нажатии вводим пробел
        button.setOnClickListener {
            currentInputConnection.commitText(" ", 1)
        }
        
        // Создаем корневой контейнер
        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        
        // Добавляем пружину (пустое пространство), которая будет "толкать" кнопку вниз
        val spaceView = View(this)
        val spaceParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1.0f // Вес, который заставит View занять всё доступное пространство
        )
        rootLayout.addView(spaceView, spaceParams)
        
        // Устанавливаем параметры для кнопки
        val buttonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.keyboard_height)
        )
        
        // Убедимся, что кнопка интерактивна
        button.isClickable = true
        button.isFocusable = true
        
        // Добавляем кнопку в контейнер
        rootLayout.addView(button, buttonParams)
        
        Log.d(TAG, "Создан вид клавиатуры с высотой: ${resources.getDimensionPixelSize(R.dimen.keyboard_height)}px")
        
        return rootLayout
    }
    
    /**
     * Переопределение параметров окна для позиционирования клавиатуры внизу экрана
     */
    override fun onComputeInsets(outInsets: Insets) {
        // Просто устанавливаем базовые параметры для рендеринга, 
        // но не ограничиваем область взаимодействия
        
        if (isInputViewShown) {
            outInsets.contentTopInsets = outInsets.visibleTopInsets
            
            // Важно - используем TOUCHABLE_INSETS_VISIBLE для обеспечения 
            // работы касаний только в видимой части клавиатуры
            outInsets.touchableInsets = Insets.TOUCHABLE_INSETS_VISIBLE
            outInsets.touchableRegion.setEmpty()
            
            Log.d(TAG, "Настроены базовые insets для работы клавиатуры")
        }
    }
    
    override fun onStartInputView(info: android.view.inputmethod.EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "SimpleIME: клавиатура показана")
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        return false // Отключаем полноэкранный режим
    }
    
    // Этот метод сообщает системе, что клавиатура должна быть видна
    override fun onEvaluateInputViewShown(): Boolean {
        return true // Всегда показываем клавиатуру
    }
} 