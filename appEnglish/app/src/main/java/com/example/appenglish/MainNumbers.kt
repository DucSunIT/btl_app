package com.example.appenglish

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainNumbers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_numbers)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgList = listOf(
            R.drawable.eight,
            R.drawable.five,
            R.drawable.four,
            R.drawable.nine,
            R.drawable.one,
            R.drawable.seven,
            R.drawable.six,
            R.drawable.ten,
            R.drawable.three,
            R.drawable.two
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordNumbers = repository.getVocabularyByCategory("numbers")
        val listNumbers = repository.generateInfoVocaList(getWordNumbers, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listNumbers)
        val gvNumbers= findViewById<GridView>(R.id.gvNumbers)
        gvNumbers.adapter = customVocabulary


        val imgPivNumbers = findViewById<ImageView>(R.id.imgPivNumbers)
        imgPivNumbers.setOnClickListener {
            finish()
        }
    }
}