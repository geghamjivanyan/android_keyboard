package com.gegham.phoneimckeyboard

import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Класс для работы с API автодополнения
 */
class AutoCompleteAPI {
    
    private val TAG = "AutoCompleteAPI"
    private val API_URL = "https://phonemics.net/api/search/"
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    /**
     * Интерфейс для получения результатов автодополнения
     */
    interface AutoCompleteCallback {
        fun onSuggestionsReceived(suggestions: List<String>, rhythms: List<String>)
        fun onError(error: String)
    }
    
    /**
     * Отправляет запрос на автодополнение
     * @param text Текст для поиска
     * @param rhythms Ритмы для поиска
     * @param callback Колбэк для получения результатов
     */
    fun getSuggestions(text: String, rhythms: List<String>, callback: AutoCompleteCallback) {
        Log.d(TAG, "=== НАЧАЛО ЗАПРОСА API ===")
        Log.d(TAG, "Входные параметры: text='$text', rhythms=$rhythms")
        
        if (text.isEmpty()) {
            Log.d(TAG, "Текст пустой, возвращаем пустые списки")
            callback.onSuggestionsReceived(emptyList(), emptyList())
            return
        }
        
        // API calls commented out - returning empty lists
        Log.d(TAG, "API calls disabled - returning empty lists")
        callback.onSuggestionsReceived(emptyList(), emptyList())
        
        /* Commented out API call implementation
        coroutineScope.launch {
            try {
                Log.d(TAG, "Отправляем запрос для текста: '$text' и ритмов: $rhythms")
                
                val url = URL(API_URL)
                val connection = url.openConnection() as HttpURLConnection
                
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("User-Agent", "ArabicKeyboard/1.0")
                connection.doOutput = true
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                
                // Создаем JSON с запросом - отправляем text и rhythms
                val jsonRequest = JSONObject().apply {
                    put("text", text)
                    put("rhythms", org.json.JSONArray(rhythms))
                }
                
                val postData = jsonRequest.toString().toByteArray(StandardCharsets.UTF_8)
                
                Log.d(TAG, "Отправляем JSON: ${jsonRequest}")
                Log.d(TAG, "Размер данных: ${postData.size} байт")
                
                connection.setRequestProperty("Content-Length", postData.size.toString())
                
                // Отправляем данные
                val outputStream: OutputStream = connection.outputStream
                outputStream.write(postData)
                outputStream.flush()
                outputStream.close()
                
                // Получаем ответ
                val responseCode = connection.responseCode
                Log.d(TAG, "Код ответа: $responseCode")
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()
                    
                    Log.d(TAG, "Ответ от сервера: ${response}")
                    
                    // Парсим JSON ответ
                    val (suggestions, rhythms) = parseResponse(response.toString())
                    
                    Log.d(TAG, "После парсинга: suggestions=$suggestions, rhythms=$rhythms")
                    
                    withContext(Dispatchers.Main) {
                        callback.onSuggestionsReceived(suggestions, rhythms)
                    }
                    
                } else {
                    // Читаем ошибку от сервера
                    val errorStream = connection.errorStream
                    val errorResponse = if (errorStream != null) {
                        val reader = BufferedReader(InputStreamReader(errorStream))
                        val response = StringBuilder()
                        var line: String?
                        
                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        reader.close()
                        response.toString()
                    } else {
                        "Нет деталей ошибки"
                    }
                    
                    val errorMessage = "Ошибка HTTP: $responseCode - $errorResponse"
                    Log.e(TAG, errorMessage)
                    
                    withContext(Dispatchers.Main) {
                        callback.onError(errorMessage)
                    }
                }
                
                connection.disconnect()
                
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при запросе автодополнения", e)
                
                withContext(Dispatchers.Main) {
                    callback.onError("Ошибка сети: ${e.message}")
                }
            }
        }
        */
    }
    
    /**
     * Парсит JSON ответ от сервера
     * @param jsonResponse JSON строка от сервера
     * @return Пара (suggestions, rhythms)
     */
    private fun parseResponse(jsonResponse: String): Pair<List<String>, List<String>> {
        return try {
            val jsonObject = JSONObject(jsonResponse)
            
            val suggestions = mutableListOf<String>()
            val rhythms = mutableListOf<String>()
            
            // Проверяем, есть ли поле data
            if (jsonObject.has("data")) {
                val dataObject = jsonObject.getJSONObject("data")
                
                // Парсим suggestions из data
                if (dataObject.has("suggestions")) {
                    val suggestionsArray = dataObject.getJSONArray("suggestions")
                    for (i in 0 until suggestionsArray.length()) {
                        suggestions.add(suggestionsArray.getString(i))
                    }
                }
                
                // Парсим rhythms из data
                if (dataObject.has("rhythms")) {
                    val rhythmsArray = dataObject.getJSONArray("rhythms")
                    for (i in 0 until rhythmsArray.length()) {
                        rhythms.add(rhythmsArray.getString(i))
                    }
                }
            } else {
                // Старый формат - ищем в корне
                if (jsonObject.has("suggestions")) {
                    val suggestionsArray = jsonObject.getJSONArray("suggestions")
                    for (i in 0 until suggestionsArray.length()) {
                        suggestions.add(suggestionsArray.getString(i))
                    }
                }
                
                if (jsonObject.has("rhythms")) {
                    val rhythmsArray = jsonObject.getJSONArray("rhythms")
                    for (i in 0 until rhythmsArray.length()) {
                        rhythms.add(rhythmsArray.getString(i))
                    }
                }
            }
            
            Log.d(TAG, "Распарсено suggestions: $suggestions")
            Log.d(TAG, "Распарсено rhythms: $rhythms")
            
            Pair(suggestions, rhythms)
            
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при парсинге JSON", e)
            Pair(emptyList(), emptyList())
        }
    }
    
    /**
     * Отменяет все текущие запросы
     */
    fun cancelAllRequests() {
        coroutineScope.cancel()
    }
} 