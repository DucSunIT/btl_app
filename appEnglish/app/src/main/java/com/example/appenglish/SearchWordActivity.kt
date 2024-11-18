package com.example.appenglish

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.internal.ViewUtils.hideKeyboard


class SearchWordActivity : AppCompatActivity() {
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
        previousActivity()
        checkEmptyWords()
    }

    /*Hàm có chức năng xử lý sự kiện khi click vào mũi tên quay lại
    * ở SearchWordActivity*/
    private fun previousActivity() {
        val imgPivSearch = findViewById<ImageView>(R.id.imgPivSearchWord)
        imgPivSearch.setOnClickListener {
            finish()
        }
    }

    private fun checkEmptyWords() {
        val edtSearchWord = findViewById<EditText>(R.id.edtSearch)
        edtSearchWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = edtSearchWord.text.toString().trim()
                if (searchQuery.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }


        val imgSearch = findViewById<ImageView>(R.id.imgSearchWord)
        imgSearch.setOnClickListener {
            Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show()
        }
    }




}