package com.example.appenglish

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appenglish.databinding.ActivityMainBinding


class SearchWordEnglish : AppCompatActivity() {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_word_english)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutScreen2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchBar = findViewById<EditText>(R.id.edtSearch)
        val imageViewSearch = findViewById<ImageView>(R.id.imgSearch)

        // Khi nhấn vào ImageView, kích hoạt EditText và hiển thị bàn phím
        imageViewSearch.setOnClickListener {
            searchBar.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)
        }

    }
}