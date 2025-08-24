package com.gegham.phoneimckeyboard

import android.graphics.Color

/**
 * Класс для управления раскладками клавиатуры
 */
class KeyboardLayoutManager {
    
    // Раскладки клавиатуры
    val keyboard1: Array<Array<Key>> = arrayOf(
        arrayOf(
            Key(english = "a", arabic = "", color = Key.WHITE, action = KeyAction.DELETE),
            Key(english = "b", arabic = "تشكيل", color = Key.BLUE, action = KeyAction.SWITCH_KEYBOARD),
            Key(english = "c", arabic = "ل", color = Key.YELLOW),
            Key(english = "d", arabic = "َ", color = Key.RED),
            Key(english = "e", arabic = "ُ", color = Key.RED),
            Key(english = "f", arabic = "ِ", color = Key.RED)
        ),
        arrayOf(
            Key(english = "g", arabic = "ف", color = Key.PURPLE),
            Key(english = "h", arabic = "ن", color = Key.YELLOW),
            Key(english = "i", arabic = "ر", color = Key.YELLOW),
            Key(english = "j", arabic = "س", color = Key.YELLOW),
            Key(english = "k", arabic = "ء", color = Key.BLUE),
            Key(english = "l", arabic = "•", color = Key.BLUE, action = KeyAction.DOT)
        ),
        arrayOf(
            Key(english = "m", arabic = "م", color = Key.PURPLE),
            Key(english = "n", arabic = "ٮ", color = Key.PURPLE),
            Key(english = "o", arabic = "د", color = Key.PURPLE),
            Key(english = "p", arabic = "ص", color = Key.YELLOW),
            Key(english = "q", arabic = "ح", color = Key.YELLOW),
            Key(english = "r", arabic = "ه", color = Key.YELLOW)
        ),
        arrayOf(
            Key(english = "s", arabic = "ـــ", color = Key.WHITE, action = KeyAction.SPACE),
            Key(english = "t", arabic = "ط", color = Key.PURPLE),
            Key(english = "u", arabic = "ك", color = Key.GREEN),
            Key(english = "v", arabic = "ق", color = Key.GREEN),
            Key(english = "w", arabic = "ع", color = Key.YELLOW),
            Key(english = "x", arabic = "↵", color = Key.WHITE, action = KeyAction.RETURN)
        )
    )
    
    val keyboard2: Array<Array<Key>> = arrayOf(
        arrayOf(
            Key(english = "a", arabic = "", color = Key.WHITE, action = KeyAction.DELETE),
            Key(english = "b", arabic = "إهمال", color = Key.BLUE, action = KeyAction.SWITCH_KEYBOARD),
            Key(english = "c", arabic = "ل", color = Key.YELLOW),
            Key(english = "d", arabic = "ا", color = Key.RED),
            Key(english = "e", arabic = "و", color = Key.RED),
            Key(english = "f", arabic = "ي", color = Key.RED)
        ),
        arrayOf(
            Key(english = "g", arabic = "ف", color = Key.PURPLE),
            Key(english = "h", arabic = "ن", color = Key.YELLOW),
            Key(english = "i", arabic = "ر", color = Key.YELLOW),
            Key(english = "j", arabic = "س", color = Key.YELLOW),
            Key(english = "k", arabic = "ء", color = Key.BLUE),
            Key(english = "l", arabic = "•", color = Key.BLUE, action = KeyAction.DOT)
        ),
        arrayOf(
            Key(english = "m", arabic = "م", color = Key.PURPLE),
            Key(english = "n", arabic = "ٮ", color = Key.PURPLE),
            Key(english = "o", arabic = "د", color = Key.PURPLE),
            Key(english = "p", arabic = "ص", color = Key.YELLOW),
            Key(english = "q", arabic = "ح", color = Key.YELLOW),
            Key(english = "r", arabic = "ه", color = Key.YELLOW)
        ),
        arrayOf(
            Key(english = "s", arabic = "ـــ", color = Key.WHITE, action = KeyAction.SPACE),
            Key(english = "t", arabic = "ط", color = Key.PURPLE),
            Key(english = "u", arabic = "ك", color = Key.GREEN),
            Key(english = "v", arabic = "ق", color = Key.GREEN),
            Key(english = "w", arabic = "ع", color = Key.YELLOW),
            Key(english = "x", arabic = "↵", color = Key.WHITE, action = KeyAction.RETURN)
        )
    )
    
    var currentKeyboard = 1
    
    /**
     * Получить текущую раскладку клавиатуры
     */
    fun getCurrentKeyboard(): Array<Array<Key>> {
        return if (currentKeyboard == 1) keyboard1 else keyboard2
    }
    
    /**
     * Переключить раскладку клавиатуры
     */
    fun switchKeyboard() {
        currentKeyboard = if (currentKeyboard == 1) 2 else 1
    }
    
    /**
     * Получить ключ по координатам
     */
    fun getKey(row: Int, col: Int): Key? {
        val currentLayout = getCurrentKeyboard()
        return if (row >= 0 && row < currentLayout.size && 
                   col >= 0 && col < currentLayout[row].size) {
            currentLayout[row][col]
        } else {
            null
        }
    }
}