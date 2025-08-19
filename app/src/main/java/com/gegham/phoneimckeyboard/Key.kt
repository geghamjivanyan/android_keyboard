package com.gegham.phoneimckeyboard

import android.graphics.Color

/**
 * Класс для представления клавиши в арабской клавиатуре
 */
data class Key(
    val english: String,
    val arabic: String,
    val color: Int,
    val action: KeyAction? = null
) {
    companion object {
        // Цвета
        val WHITE = Color.parseColor("#FAFAFA")
        val YELLOW = Color.parseColor("#E6E6B4")
        val RED = Color.parseColor("#F0C8C8")
        val PURPLE = Color.parseColor("#D397D3")
        val GREEN = Color.parseColor("#C8F0C8")
        val BLUE = Color.parseColor("#C8D2FA")
    }
} 