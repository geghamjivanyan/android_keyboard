package com.gegham.phoneimckeyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * View для отображения предложений автодополнения
 * Компактный вид: две горизонтальные строки над клавиатурой
 */
class AutoCompleteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "AutoCompleteView"
    
    // Списки предложений и ритмов
    private var suggestions: List<String> = emptyList()
    private var rhythms: List<String> = emptyList()
    
    // Выбранное предложение
    private var selectedIndex: Int = -1
    private var selectedSection: String = "" // "suggestions" или "rhythms"
    
    // Фиксированная высота (100dp)
    private val fixedHeight = (100 * resources.displayMetrics.density).toInt()
    private val rowHeight = fixedHeight / 2f
    private val padding = 8f * resources.displayMetrics.density
    private val cornerRadius = 6f * resources.displayMetrics.density
    
    // Параметры для рисования
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#F5F5F5") // светло-серый фон
        style = Paint.Style.FILL
    }
    
    private val borderPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#E0E0E0") // серый бордер
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }
    
    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textSize = 16f * resources.displayMetrics.density
    }
    
    private val selectedTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textSize = 16f * resources.displayMetrics.density
    }
    
    private val selectedBackgroundPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#2196F3") // синий для выбранного
        style = Paint.Style.FILL
    }
    
    private val rhythmTextPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#666666") // темно-серый для ритмов
        textSize = 14f * resources.displayMetrics.density
    }
    
    // Колбэк для выбора предложения
    var onSuggestionSelected: ((String) -> Unit)? = null
    var onRhythmSelected: ((String) -> Unit)? = null
    
    /**
     * Устанавливает списки предложений и ритмов
     */
    fun setSuggestionsAndRhythms(newSuggestions: List<String>, newRhythms: List<String>) {
        Log.d(TAG, "setSuggestionsAndRhythms вызван: suggestions=${newSuggestions.size}, rhythms=${newRhythms.size}")
        Log.d(TAG, "Предложения: $newSuggestions")
        Log.d(TAG, "Ритмы: $newRhythms")
        
        suggestions = newSuggestions
        rhythms = newRhythms
        selectedIndex = -1
        selectedSection = ""
        
        Log.d(TAG, "Обновляем View: invalidate() и requestLayout()")
        invalidate()
        requestLayout()
    }
    
    /**
     * Очищает предложения
     */
    fun clearSuggestions() {
        suggestions = emptyList()
        rhythms = emptyList()
        selectedIndex = -1
        selectedSection = ""
        invalidate()
        requestLayout()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        Log.d(TAG, "onMeasure: width=$width, fixedHeight=$fixedHeight")
        setMeasuredDimension(width, fixedHeight)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        Log.d(TAG, "onDraw вызван: suggestions=${suggestions.size}, rhythms=${rhythms.size}, width=$width, height=$height")
        
        if (suggestions.isEmpty() && rhythms.isEmpty()) {
            Log.d(TAG, "Нет данных для отрисовки")
            return
        }
        
        // Фон
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
        
        // Первая строка — предложения
        if (suggestions.isNotEmpty()) {
            Log.d(TAG, "Рисуем ${suggestions.size} предложений")
            var x = padding
            val y = padding
            val maxItems = 8 // максимум 8 элементов в строке
            
            for (i in 0 until minOf(suggestions.size, maxItems)) {
                val suggestion = suggestions[i]
                Log.d(TAG, "Рисуем предложение $i: $suggestion")
                
                val paint = if (selectedSection == "suggestions" && selectedIndex == i) selectedTextPaint else textPaint
                val bgPaint = if (selectedSection == "suggestions" && selectedIndex == i) selectedBackgroundPaint else borderPaint
                
                val textWidth = paint.measureText(suggestion) + padding * 2
                val rect = RectF(x, y, x + textWidth, y + rowHeight - padding)
                
                if (selectedSection == "suggestions" && selectedIndex == i) {
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, selectedBackgroundPaint)
                } else {
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)
                }
                
                canvas.drawText(suggestion, x + padding, y + rowHeight / 2 + paint.textSize / 3, paint)
                x += textWidth + padding
            }
        }
        
        // Вторая строка — ритмы
        if (rhythms.isNotEmpty()) {
            Log.d(TAG, "Рисуем ${rhythms.size} ритмов")
            var x = padding
            val y = rowHeight + padding
            val maxItems = 8 // максимум 8 элементов в строке
            
            for (i in 0 until minOf(rhythms.size, maxItems)) {
                val rhythm = rhythms[i]
                Log.d(TAG, "Рисуем ритм $i: $rhythm")
                
                val paint = if (selectedSection == "rhythms" && selectedIndex == i) selectedTextPaint else rhythmTextPaint
                val bgPaint = if (selectedSection == "rhythms" && selectedIndex == i) selectedBackgroundPaint else borderPaint
                
                val textWidth = paint.measureText(rhythm) + padding * 2
                val rect = RectF(x, y, x + textWidth, y + rowHeight - padding)
                
                if (selectedSection == "rhythms" && selectedIndex == i) {
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, selectedBackgroundPaint)
                } else {
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint)
                }
                
                canvas.drawText(rhythm, x + padding, y + rowHeight / 2 + paint.textSize / 3, paint)
                x += textWidth + padding
            }
        }
        
        Log.d(TAG, "onDraw завершен")
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val (section, index) = getItemAt(event.y)
                if (index >= 0) {
                    selectedSection = section
                    selectedIndex = index
                    invalidate()
                }
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                if (selectedIndex >= 0) {
                    when (selectedSection) {
                        "suggestions" -> {
                            if (selectedIndex < suggestions.size) {
                                val suggestion = suggestions[selectedIndex]
                                Log.d(TAG, "Выбрано предложение: $suggestion")
                                onSuggestionSelected?.invoke(suggestion)
                            }
                        }
                        "rhythms" -> {
                            if (selectedIndex < rhythms.size) {
                                val rhythm = rhythms[selectedIndex]
                                Log.d(TAG, "Выбран ритм: $rhythm")
                                onRhythmSelected?.invoke(rhythm)
                            }
                        }
                    }
                    selectedIndex = -1
                    selectedSection = ""
                    invalidate()
                }
                return true
            }
            
            MotionEvent.ACTION_CANCEL -> {
                selectedIndex = -1
                selectedSection = ""
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    
    /**
     * Определяет, какой элемент находится по координате Y
     */
    private fun getItemAt(y: Float): Pair<String, Int> {
        if (y < rowHeight) {
            // Первая строка - предложения
            if (suggestions.isNotEmpty()) {
                val itemWidth = (width - padding * 2) / minOf(suggestions.size, 8)
                val index = ((y - padding) / (rowHeight - padding * 2)).toInt()
                if (index >= 0 && index < suggestions.size) {
                    return Pair("suggestions", index)
                }
            }
        } else {
            // Вторая строка - ритмы
            if (rhythms.isNotEmpty()) {
                val itemWidth = (width - padding * 2) / minOf(rhythms.size, 8)
                val index = ((y - rowHeight - padding) / (rowHeight - padding * 2)).toInt()
                if (index >= 0 && index < rhythms.size) {
                    return Pair("rhythms", index)
                }
            }
        }
        return Pair("", -1)
    }
} 