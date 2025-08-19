package com.gegham.phoneimckeyboard

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.app.Dialog
import android.inputmethodservice.InputMethodService.Insets
import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout

/**
 * Основной класс IME для арабской клавиатуры
 */
class ArabicKeyboardIME : InputMethodService() {
    
    private val TAG = "ArabicKeyboardIME"
    
    private lateinit var keyboardView: HexKeyboardView
    // AutoCompleteView removed from keyboard
    // private lateinit var autoCompleteView: AutoCompleteView
    private val textTransformer = ArabicTextTransformer()
    private val dotTransformer = ArabicDotTransformer()
    // API calls disabled
    // private val autoCompleteAPI = AutoCompleteAPI()
    
    // Для отложенных запросов автодополнения
    private val handler = Handler(Looper.getMainLooper())
    private var autoCompleteRunnable: Runnable? = null
    
    // Текущий текст для автодополнения
    private var currentText = ""
    private var currentRhythms = mutableListOf<String>()
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ArabicKeyboardIME создан")
    }
    
    override fun onCreateInputView(): View {
        Log.d(TAG, "Создаем вид арабской клавиатуры")
        
        // Создаем клавиатуру напрямую без дополнительных контейнеров
        keyboardView = HexKeyboardView(this)
        
        // Устанавливаем обработчики
        keyboardView.onKeyPressed = { key -> handleKeyPress(key) }
        
        Log.d(TAG, "Создан вид клавиатуры без автодополнения")
        return keyboardView
    }
    
    
    private fun handleKeyPress(key: Key) {
        Log.d(TAG, "Нажата клавиша: ${key.english} - ${key.arabic}")
        
        // Анимация нажатия происходит в самом HexKeyboardView
        
        // Обработка нажатий
        when (key.action) {
            KeyAction.DELETE -> {
                val beforeCursor = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString() ?: ""
                if (beforeCursor.length <= 1) {
                    // If only space or empty, don't delete the initial space
                    if (beforeCursor == " ") {
                        // Don't delete the initial space
                        return
                    }
                }
                currentInputConnection?.deleteSurroundingText(1, 0)
                
                // If all text deleted, add space back
                val textAfterDelete = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString() ?: ""
                if (textAfterDelete.isEmpty()) {
                    currentInputConnection?.commitText(" ", 1)
                }
                
                updateCurrentText()
                // API calls disabled
                // requestAutoComplete()
            }
            KeyAction.SWITCH_KEYBOARD -> {
                keyboardView.switchKeyboard()
            }
            KeyAction.SPACE -> {
                currentInputConnection?.commitText(" ", 1)
                updateCurrentText()
                // API calls disabled
                // requestAutoComplete()
            }
            KeyAction.ENTER, KeyAction.RETURN -> {
                currentInputConnection?.commitText("\n", 1)
                updateCurrentText()
                // API calls disabled
                // clearAutoComplete()
            }
            KeyAction.DOT -> {
                handleDotTransformation()
                updateCurrentText()
                // API calls disabled
                // requestAutoComplete()
            }
            KeyAction.SHIFT, KeyAction.SWITCH_LANGUAGE -> {
                // Эти действия не используются в арабской клавиатуре
                Log.d(TAG, "Не используемое действие: ${key.action}")
            }
            null -> {
                // Вставляем текст
                val textToInsert = if (key.arabic.isEmpty()) key.english else key.arabic
                currentInputConnection?.commitText(textToInsert, 1)
                
                // Применяем трансформации
                val wholeText = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString()
                if (wholeText != null && wholeText.isNotEmpty()) {
                    val transformedText = textTransformer.convert(wholeText)
                    if (transformedText != wholeText) {
                        // Удаляем весь текст и вставляем трансформированный
                        currentInputConnection?.deleteSurroundingText(wholeText.length, 0)
                        currentInputConnection?.commitText(transformedText, 1)
                    }
                }
                
                updateCurrentText()
                // API calls disabled
                // requestAutoComplete()
            }
        }
    }
    
    /**
     * Обновляет текущий текст для автодополнения
     */
    private fun updateCurrentText() {
        val beforeCursor = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString() ?: ""
        val afterCursor = currentInputConnection?.getTextAfterCursor(1000, 0)?.toString() ?: ""
        
        // Используем весь текст перед курсором для автодополнения
        currentText = beforeCursor
        Log.d(TAG, "Обновлен текущий текст для автодополнения: '$currentText'")
        Log.d(TAG, "Текст после курсора: '$afterCursor'")
    }
    
    // API calls and autocomplete functionality disabled
    /* Commented out requestAutoComplete method
    private fun requestAutoComplete() {
        // Отменяем предыдущий запрос
        autoCompleteRunnable?.let { handler.removeCallbacks(it) }
        
        // Создаем новый отложенный запрос
        autoCompleteRunnable = Runnable {
            Log.d(TAG, "Проверяем условие для API: currentText='$currentText', length=${currentText.length}, minLength=1")
            
            if (currentText.isNotEmpty() && currentText.length >= 1) {
                Log.d(TAG, "Отправляем запрос автодополнения для текста: '$currentText' и ритмов: $currentRhythms")
                
                // Используем реальный API
                autoCompleteAPI.getSuggestions(currentText, currentRhythms, object : AutoCompleteAPI.AutoCompleteCallback {
                    override fun onSuggestionsReceived(suggestions: List<String>, rhythms: List<String>) {
                        Log.d(TAG, "Получены предложения: $suggestions")
                        Log.d(TAG, "Получены ритмы: $rhythms")
                        
                        // Если API возвращает пустые массивы, показываем тестовые данные
                        if (suggestions.isEmpty() && rhythms.isEmpty()) {
                            Log.d(TAG, "API вернул пустые данные, показываем тестовые")
                            val testSuggestions = listOf("API пустой - тест 1", "API пустой - тест 2", "API пустой - тест 3")
                            val testRhythms = listOf("тест ритм 1", "тест ритм 2", "тест ритм 3")
                            autoCompleteView.setSuggestionsAndRhythms(testSuggestions, testRhythms)
                        } else {
                            autoCompleteView.setSuggestionsAndRhythms(suggestions, rhythms)
                        }
                    }
                    
                    override fun onError(error: String) {
                        Log.e(TAG, "Ошибка автодополнения: $error")
                        
                        // При ошибке показываем тестовые данные
                        val testSuggestions = listOf("Ошибка API - тест 1", "Ошибка API - тест 2", "Ошибка API - тест 3")
                        val testRhythms = listOf("ошибка ритм 1", "ошибка ритм 2", "ошибка ритм 3")
                        autoCompleteView.setSuggestionsAndRhythms(testSuggestions, testRhythms)
                    }
                })
            } else {
                Log.d(TAG, "Условие для API не выполнено, очищаем автодополнение")
                autoCompleteView.clearSuggestions()
            }
        }
        
        // Запускаем запрос через 500ms после последнего нажатия
        handler.postDelayed(autoCompleteRunnable!!, 500)
    }
    */
    
    // Autocomplete handler methods disabled
    /* Commented out autocomplete handlers
    private fun handleSuggestionSelected(suggestion: String) {
        Log.d(TAG, "Выбрано предложение: $suggestion")
        
        // Удаляем текущее слово и вставляем предложение
        val beforeCursor = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString() ?: ""
        val afterCursor = currentInputConnection?.getTextAfterCursor(1000, 0)?.toString() ?: ""
        
        // Находим последнее слово
        val words = beforeCursor.split("\\s+".toRegex())
        val lastWord = if (words.isNotEmpty()) words.last() else ""
        
        if (lastWord.isNotEmpty()) {
            // Удаляем последнее слово
            currentInputConnection?.deleteSurroundingText(lastWord.length, 0)
        }
        
        // Вставляем предложение
        currentInputConnection?.commitText(suggestion, 1)
        
        // Очищаем автодополнение
        autoCompleteView.clearSuggestions()
        
        // Обновляем текущий текст
        updateCurrentText()
    }
    
    private fun handleRhythmSelected(rhythm: String) {
        Log.d(TAG, "Выбран ритм: $rhythm")
        
        // Добавляем ритм в список текущих ритмов
        if (!currentRhythms.contains(rhythm)) {
            currentRhythms.add(rhythm)
        }
        
        // Очищаем автодополнение
        autoCompleteView.clearSuggestions()
        
        // Запрашиваем новые предложения с обновленными ритмами
        requestAutoComplete()
    }
    
    private fun clearAutoComplete() {
        autoCompleteView.clearSuggestions()
    }
    */
    
    // Обработка нажатия точки с трансформацией
    private fun handleDotTransformation() {
        val lastChar = currentInputConnection?.getTextBeforeCursor(1, 0)?.toString()?.lastOrNull()
        
        if (lastChar != null) {
            val replacement = dotTransformer.handleDotTransformation(lastChar)
            
            if (replacement != null) {
                currentInputConnection?.deleteSurroundingText(1, 0)
                currentInputConnection?.commitText(replacement, 1)
            } else {
                currentInputConnection?.commitText(".", 1)
            }
        } else {
            currentInputConnection?.commitText(".", 1)
        }
    }
    
    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d(TAG, "Арабская клавиатура показана")
        
        // Check if input field is empty and add initial space
        val currentText = currentInputConnection?.getTextBeforeCursor(1000, 0)?.toString() ?: ""
        if (currentText.isEmpty()) {
            currentInputConnection?.commitText(" ", 1)
            Log.d(TAG, "Added initial space to empty input field")
        }
        
        // Обновляем текущий текст при показе клавиатуры
        updateCurrentText()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // API calls disabled
        // autoCompleteAPI.cancelAllRequests()
        autoCompleteRunnable?.let { handler.removeCallbacks(it) }
    }
    
    override fun onEvaluateFullscreenMode(): Boolean {
        return false // Отключаем полноэкранный режим
    }
    
    // Этот метод сообщает системе, что клавиатура должна быть видна
    override fun onEvaluateInputViewShown(): Boolean {
        return true // Всегда показываем клавиатуру
    }
} 