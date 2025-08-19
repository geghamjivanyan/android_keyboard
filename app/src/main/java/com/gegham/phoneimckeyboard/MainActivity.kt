package com.gegham.phoneimckeyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupUI()
    }
    
    private fun setupUI() {
        // Заголовок
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = getString(R.string.app_name)
        
        // Инструкции
        val instructionsTextView = findViewById<TextView>(R.id.instructionsTextView)
        instructionsTextView.text = getString(R.string.settings_instructions)
        
        // Кнопка настроек
        val settingsButton = findViewById<Button>(R.id.openSettingsButton)
        settingsButton.text = getString(R.string.open_settings)
        settingsButton.setOnClickListener {
            // Открываем настройки IME
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        }
    }
} 