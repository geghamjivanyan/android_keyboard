package com.gegham.phoneimckeyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Представление шестиугольной клавиатуры в стиле iOS
 */
class HexKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "HexKeyboardView"
    
    // Менеджер раскладки клавиатуры
    private val layoutManager = KeyboardLayoutManager()
    
    // Преобразователи текста
    private val textTransformer = ArabicTextTransformer()
    private val dotTransformer = ArabicDotTransformer()
    
    // Размеры шестиугольников
    private var hexSize = 0f
    private var horizontalSpacing = 0f
    private var verticalSpacing = 0f
    
    // Отслеживание нажатия
    private var pressedRow = -1
    private var pressedCol = -1
    
    // Параметры для рисования
    private val hexagonPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.DKGRAY
        strokeWidth = 0.5f
    }
    
    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }
    
    private val subTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        color = Color.DKGRAY
        alpha = 180
    }
    
    // Слушатель для обработки нажатий на клавиши
    var onKeyPressed: ((Key) -> Unit)? = null
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Получаем доступную ширину
        val width = MeasureSpec.getSize(widthMeasureSpec)
        
        // Рассчитываем необходимую высоту для клавиатуры
        // 5 рядов клавиш + отступы
        val desiredHeight = (width * 0.65f).toInt() // Пропорциональная высота
        
        // Устанавливаем размеры представления
        setMeasuredDimension(width, desiredHeight)
    }
    
    // Рассчитываем размеры при изменении размера представления
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        // Рассчитываем оптимальный размер шестиугольника
        val numColumns = 6f // количество колонок
        val availableWidth = width.toFloat() - 12f // отступы по краям
        
        // Ширина шестиугольника = размер * cos(30°) * 2 ≈ размер * 1.732
        hexSize = availableWidth / (numColumns + 0.5f) / 0.866f
        
        horizontalSpacing = hexSize * 0.866f // cos(30°) * hexSize
        verticalSpacing = hexSize * 0.75f // для плотного расположения
        
        // Устанавливаем размер шрифта в зависимости от размера клавиш
        val fontSize = hexSize / 2.5f
        textPaint.textSize = fontSize
        subTextPaint.textSize = fontSize * 0.4f
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Получаем текущую раскладку
        val currentLayout = layoutManager.getCurrentKeyboard()
        
        // Начальные координаты для первого шестиугольника
        val startX = 6f + hexSize * 0.433f // половина ширины шестиугольника
        val startY = 8f
        
        // Рисуем шестиугольники для каждой клавиши
        for (rowIndex in currentLayout.indices) {
            // Смещение для нечетных рядов (сдвиг вправо)
            val rowOffset = if (rowIndex % 2 == 1) horizontalSpacing / 2 else 0f
            
            for (colIndex in currentLayout[rowIndex].indices) {
                val key = currentLayout[rowIndex][colIndex]
                
                // Рассчитываем положение шестиугольника
                val x = startX + rowOffset + colIndex * horizontalSpacing
                val y = startY + rowIndex * verticalSpacing + hexSize / 2
                
                // Проверяем, нажата ли клавиша
                val isPressed = rowIndex == pressedRow && colIndex == pressedCol
                
                // Рисуем шестиугольник
                drawHexagonForKey(canvas, key, x, y, isPressed)
            }
        }
    }
    
    private fun drawHexagonForKey(canvas: Canvas, key: Key, centerX: Float, centerY: Float, isPressed: Boolean) {
        // Создаем путь для шестиугольника
        val hexPath = Path()
        val radius = hexSize / 2 - 1
        
        // Строим шестиугольник
        for (i in 0 until 6) {
            val angle = i * Math.PI / 3.0 - Math.PI / 6.0
            val x = centerX + radius * cos(angle).toFloat()
            val y = centerY + radius * sin(angle).toFloat()
            
            if (i == 0) {
                hexPath.moveTo(x, y)
            } else {
                hexPath.lineTo(x, y)
            }
        }
        hexPath.close()
        
        // Устанавливаем цвет в зависимости от нажатия
        hexagonPaint.color = if (isPressed) {
            darkenColor(key.color)
        } else {
            key.color
        }
        
        // Рисуем шестиугольник
        canvas.drawPath(hexPath, hexagonPaint)
        canvas.drawPath(hexPath, strokePaint)
        
        // Определяем текст для клавиши
        val title = when (key.action) {
            KeyAction.DELETE -> "⌫"
            KeyAction.SWITCH_KEYBOARD -> key.arabic
            KeyAction.DOT -> "•"
            KeyAction.SPACE -> "ـــ"
            KeyAction.ENTER, KeyAction.RETURN -> "↵"
            KeyAction.SHIFT, KeyAction.SWITCH_LANGUAGE -> key.arabic // Не используются, но для совместимости
            null -> if (key.arabic.isEmpty()) key.english else key.arabic
        }
        
        // Размер шрифта в зависимости от контента
        if (key.arabic == "تشكيل") {
            textPaint.textSize = hexSize / 2.5f * 0.5f
        } else if (key.action != null) {
            textPaint.textSize = hexSize / 2.5f * 0.85f
        } else {
            textPaint.textSize = hexSize / 2.5f
        }
        
        // Рисуем основной текст клавиши
        canvas.drawText(title, centerX, centerY + textPaint.textSize / 3, textPaint)
        
        // English letters removed from display
    }
    
    // Функция для затемнения цвета при нажатии
    private fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.8f // уменьшаем яркость
        return Color.HSVToColor(hsv)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y
                val hitKey = findKeyAt(touchX, touchY)
                
                if (hitKey != null) {
                    pressedRow = hitKey.first
                    pressedCol = hitKey.second
                    invalidate()
                }
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                if (pressedRow >= 0 && pressedCol >= 0) {
                    val key = layoutManager.getKey(pressedRow, pressedCol)
                    if (key != null) {
                        onKeyPressed?.invoke(key)
                    }
                    
                    // Сбрасываем нажатие
                    pressedRow = -1
                    pressedCol = -1
                    invalidate()
                }
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                val touchX = event.x
                val touchY = event.y
                val hitKey = findKeyAt(touchX, touchY)
                
                if (hitKey != null) {
                    val newRow = hitKey.first
                    val newCol = hitKey.second
                    
                    if (newRow != pressedRow || newCol != pressedCol) {
                        pressedRow = newRow
                        pressedCol = newCol
                        invalidate()
                    }
                } else {
                    if (pressedRow >= 0 || pressedCol >= 0) {
                        pressedRow = -1
                        pressedCol = -1
                        invalidate()
                    }
                }
                return true
            }
            
            MotionEvent.ACTION_CANCEL -> {
                pressedRow = -1
                pressedCol = -1
                invalidate()
                return true
            }
        }
        
        return super.onTouchEvent(event)
    }
    
    // Находит клавишу по координатам касания
    private fun findKeyAt(x: Float, y: Float): Pair<Int, Int>? {
        val currentLayout = layoutManager.getCurrentKeyboard()
        
        val startX = 6f + hexSize * 0.433f
        val startY = 8f
        
        for (rowIndex in currentLayout.indices) {
            val rowOffset = if (rowIndex % 2 == 1) horizontalSpacing / 2 else 0f
            
            for (colIndex in currentLayout[rowIndex].indices) {
                val centerX = startX + rowOffset + colIndex * horizontalSpacing
                val centerY = startY + rowIndex * verticalSpacing + hexSize / 2
                
                // Расстояние от центра шестиугольника до точки касания
                val distance = sqrt(
                    (x - centerX) * (x - centerX) + 
                    (y - centerY) * (y - centerY)
                )
                
                // Если расстояние меньше радиуса шестиугольника, это попадание
                if (distance <= hexSize / 2) {
                    return Pair(rowIndex, colIndex)
                }
            }
        }
        
        return null
    }
    
    // Обработка нажатия точки с трансформацией
    fun handleDotTransformation(lastChar: Char): String? {
        return dotTransformer.handleDotTransformation(lastChar)
    }
    
    // Метод для переключения раскладки
    fun switchKeyboard() {
        layoutManager.switchKeyboard()
        invalidate()
    }
    
    // Преобразование текста с арабскими правилами
    fun convertText(text: String): String {
        return textTransformer.convert(text)
    }
} 