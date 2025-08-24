package com.gegham.phoneimckeyboard

/**
 * Класс для трансформаций точек в арабских символах
 */
class ArabicDotTransformer {
    val dotTransformations: Map<String, String> = mapOf(
        // Hamza cycle
        (0x0621.toChar().toString()) to (0x0623.toChar().toString()), // ء → أ
        (0x0623.toChar().toString()) to (0x0625.toChar().toString()), // أ → إ
        (0x0625.toChar().toString()) to (0x0626.toChar().toString()), // إ → ئ
        (0x0626.toChar().toString()) to (0x0624.toChar().toString()), // ئ → ؤ
        (0x0624.toChar().toString()) to (0x0623.toChar().toString()), // ؤ → أ

        // ح → خ → ج → ح
        (0x062D.toChar().toString()) to (0x062E.toChar().toString()),
        (0x062E.toChar().toString()) to (0x062C.toChar().toString()),
        (0x062C.toChar().toString()) to (0x062D.toChar().toString()),

        // ب cycle
        (0x066E.toChar().toString()) to (0x0628.toChar().toString()),
        (0x0628.toChar().toString()) to (0x062A.toChar().toString()),
        (0x062A.toChar().toString()) to (0x062B.toChar().toString()),
        (0x062B.toChar().toString()) to (0x066E.toChar().toString()),

        // ه ↔ ة
        (0x0647.toChar().toString()) to (0x0629.toChar().toString()),
        (0x0629.toChar().toString()) to (0x0647.toChar().toString()),

        // ص ↔ ض
        (0x0635.toChar().toString()) to (0x0636.toChar().toString()),
        (0x0636.toChar().toString()) to (0x0635.toChar().toString()),

        // ع ↔ غ
        (0x0639.toChar().toString()) to (0x063A.toChar().toString()),
        (0x063A.toChar().toString()) to (0x0639.toChar().toString()),

        // ن → ت
        (0x0646.toChar().toString()) to (0x062A.toChar().toString()),

        // No change group
        ("\u0020\u064A") to ("\u0020\u0625\u0650\u0646"),
        (0x064A.toChar().toString()) to (0x064A.toChar().toString()),
        (0x0644.toChar().toString()) to (0x0644.toChar().toString()),
        (0x0645.toChar().toString()) to (0x0645.toChar().toString()),
        (0x0643.toChar().toString()) to (0x0643.toChar().toString()),

        ("\u0020\u0648") to ("\u0020\u0623\u064F\u0646"),
        (0x0648.toChar().toString()) to (0x0648.toChar().toString()),

        // ف ↔ ق
        (0x0641.toChar().toString()) to (0x0642.toChar().toString()),
        (0x0642.toChar().toString()) to (0x0641.toChar().toString()),

        // Space + ا → Space + أَن
        ("\u0020\u0627") to ("\u0020\u0623\u064E\u0646"),
        (0x0627.toChar().toString()) to (0x0649.toChar().toString()),

        // ر ↔ ز
        (0x0631.toChar().toString()) to (0x0632.toChar().toString()),
        (0x0632.toChar().toString()) to (0x0631.toChar().toString()),

        // د ↔ ذ
        (0x062F.toChar().toString()) to (0x0630.toChar().toString()),
        (0x0630.toChar().toString()) to (0x062F.toChar().toString()),

        // ط ↔ ظ
        (0x0637.toChar().toString()) to (0x0638.toChar().toString()),
        (0x0638.toChar().toString()) to (0x0637.toChar().toString()),

        // س ↔ ش
        (0x0633.toChar().toString()) to (0x0634.toChar().toString()),
        (0x0634.toChar().toString()) to (0x0633.toChar().toString()),

        // Vowel transformations
        (0x064E.toChar().toString()) to ("\u064B\u0627\u0020"),
        (0x064F.toChar().toString()) to ("\u064C\u0020"),
        (0x0650.toChar().toString()) to ("\u064D\u0020")
    )

    /**
     * Обрабатывает трансформацию точки для последнего символа в тексте
     */
    fun handleDotTransformation(lastChar: Char): String? {
        val lastCharStr = lastChar.toString()
        return dotTransformations[lastCharStr]
    }
    
    /**
     * Обрабатывает трансформацию для нескольких символов в тексте
     */
    fun handleMultiCharTransformation(lastChars: String): String? {
        return dotTransformations[lastChars]
    }
} 