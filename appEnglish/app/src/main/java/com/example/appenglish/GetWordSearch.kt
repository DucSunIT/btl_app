package com.example.appenglish

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GetWordSearch : AppCompatActivity() {
    private lateinit var listView: ListView
    private val searchedWords = mutableListOf<String>() // Danh sách từ đã tra
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_get_word_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lvGetSearchWord) // ID của ListView trong XML
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchedWords)
        listView.adapter = adapter

        // Lấy từ được truyền qua Intent
        val searchWord = intent.getStringExtra("SEARCH_WORD")
        if (!searchWord.isNullOrEmpty()) {
            // Thêm từ vào danh sách
            searchedWords.add(searchWord)
            adapter.notifyDataSetChanged() // Cập nhật giao diện ListView
        }

    }
}