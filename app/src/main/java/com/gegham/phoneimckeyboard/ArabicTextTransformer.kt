package com.gegham.phoneimckeyboard

/**
 * Класс для трансформации арабского текста на основе правил
 */
class ArabicTextTransformer {
    private val transformationMap: Map<String, String> = mapOf(
        "لِئِ" to "لِإِ",
        "لِلئِ" to "لِلإِ",
        "لِلؤُ" to "لِلأُ",
        "الؤُ" to "الأُ",
        "الئِ" to "الإِ",
        "بِئِ" to "بِإِ",
        "أَئِ" to "أَإِ",
        "أَؤُ" to "أَأُ",
        "كَؤُ" to "كَأُ",
        "كَأِ" to "كَإِ",
        "كَئِ" to "كَإِ",
        "كَإِيب" to "كَئِيب",
        "فَأِ" to "فَإِ",
        "فَئِ" to "فَإِ",
        "فَؤُ" to "فَأُ",
        "وَئِ" to "وَإِ",
        "وَؤُ" to "وَأُ",
        
        "ِأ" to "ِئ",
        "أِ" to "ئِ",
        "ؤِ" to "ئِ",
        "ُأ" to "ُؤ",
        "أُ" to "ؤُ",
        "أَا" to "آ",
        
        "اأَ" to "اءَ",
        "بأ" to "بء",
        "دأ" to "دء",
        "ُوأ" to "ُوء",
        "َوأ" to "َوء",
        "زأ" to "زء",
        "طأ" to "طء",
        "َيأ" to "َيء",
        "يءَ" to "يئَا",
        "ِيءَ" to "ِيئَا",
        "ِيءُ" to "ِيئُ",
        "لأ" to "لء",
        "فأ" to "فء",
        "رأ" to "رء",
        "ءَ" to "َا",
        "مَاءَا" to "مَاءَ",
        "رِدَاءَا" to "رِدَاءَ",
        
        "فءً " to "فئًا ",
        "مءً " to "مئًا ",
        "لءً " to "لئًا ",
        "بءً " to "بءًا ",
        "َيءً " to "َيئًا ",
        "سءً " to " سئًا ",
        "طءً " to "طئًا ",
        "َوءً " to "َوءًا ",
        "رءً " to "رءًا ",
        "زءً " to "زءًا ",
        "دءً " to "دءًا ",
        "ذءً " to "ذءًا ",
        "ِءً " to "ِئًا ",
        "ِئً " to "ِئًا ",      
                
        "ُُ" to "ُو",
        "ُوُ" to "وُو",
        "وُوُ" to "وُوّ",
        "ِِ" to "ِي",
        "ِيِ" to "يِي",
        "يِيِ" to "يِيّ",
        " ُ" to " و",
        " ِ" to " ي",
        "َُ" to "َو",
        "اُ" to "او",
        "َِ" to "َي",
        "اِ" to "اي",
        "َُ" to "وَ",
        "َِ" to "يَ",
        
        "عَبدَل" to "عَبدال",
        "عَبدِل" to "عَبدِال",
        "عَبدُل" to "عَبدُال",
        "عَبدَالءِ" to "عَبدَالإِ",
        "عَبدَالءُ" to "عَبدَالأُ",
        "حَتَّا " to "حَتَّى ",
        "عَلا " to "عَلَى ",
        "بَلا " to "بَلَى ",
        "إلا " to "إِلَى ",
        "مَتا " to "مَتَى ",
        "عيسا " to "عِيسَى ",
        "موسا " to "مُوسَى ",
        "لَدا " to " لَدَى ",
        "أولا " to "أَولَى ",
        "رَعا " to "رَعَى ",
        "رَما " to "رَمَى ",
        "أً" to "أً ",
        "ةً" to "ةً ",
        "اً" to "ًا ",
        "ىً" to "ًى ",
        " َ" to " ا",
        "ٍ" to "ٍ ",
        "ٌ" to "ٌ ",
        "اللَاه" to "اللّه",
        "هَاأَنتُم" to "هَأَنتُم",
        "هَاأَنَا" to "هَأَنَا",
        "هَاذِهِ" to "هَذِهِ",
        "هَاؤُلَاء" to "هَؤُلَاء",
        "ذَالِك" to "ذَلِك",
        "هَاذَا" to "هَذَا",
        "لَاكِن" to "لَكِن",
        "إِلَاه" to "إِلَه",
        "الرَّحمَان" to "الرَّحمَن",
        "أُلِي" to "أُولِي",
        "أُلُو" to "أُولُو",
        "أُلَائِك" to "أُولَئِك",
        "عَمرُ" to "عَمرو"
    )
    
    private var conversionRules: List<Pair<String, String>> = transformationMap
        .toList()
        .sortedByDescending { it.first.length }

    /**
     * Конвертирует текст применяя все правила преобразования
     */
    fun convert(text: String): String {
        var result = text

        // Применяем все правила преобразования
        for (rule in conversionRules) {
            result = result.replace(rule.first, rule.second)
        }

        return result
    }

    /**
     * Альтернативный метод, который показывает какие преобразования были применены
     */
    fun convertWithLog(text: String): Pair<String, List<Pair<String, String>>> {
        var result = text
        val appliedConversions = mutableListOf<Pair<String, String>>()

        for (rule in conversionRules) {
            if (result.contains(rule.first)) {
                result = result.replace(rule.first, rule.second)
                appliedConversions.add(rule)
            }
        }

        return Pair(result, appliedConversions)
    }

    /**
     * Метод для получения всех доступных правил преобразования
     */
    fun getAllRules(): List<Pair<String, String>> {
        return conversionRules
    }

    /**
     * Метод для добавления пользовательского правила преобразования
     */
    fun addRule(from: String, to: String) {
        conversionRules = conversionRules + Pair(from, to)
        // Пересортировка для поддержания правильного порядка
        conversionRules = conversionRules.sortedByDescending { it.first.length }
    }
}