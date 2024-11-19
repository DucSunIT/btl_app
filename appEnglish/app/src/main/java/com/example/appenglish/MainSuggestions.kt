package com.example.appenglish

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainSuggestions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_suggestions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val list = mutableListOf<SugesstionsWord>()
        list.add(SugesstionsWord("love", "ipaLove", "lovelove"))
        list.add(SugesstionsWord("test2", "ipaLove", "test111"))
        list.add(SugesstionsWord("test3", "ipaLove", "test222"))
        list.add(SugesstionsWord("test4", "ipaLove", "test33"))

        val adt = CustomSugesstionWord(this, list)
    }
}