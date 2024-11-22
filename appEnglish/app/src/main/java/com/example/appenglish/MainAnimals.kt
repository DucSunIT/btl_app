package com.example.appenglish

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainAnimals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animals)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.bear,
            R.drawable.cat,
            R.drawable.voi,
            R.drawable.fox,
            R.drawable.huou,
            R.drawable.hama,
            R.drawable.lion,
            R.drawable.bachtuoc,
            R.drawable.nhim,
            R.drawable.soc
        )
        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordAnimals = repository.getVocabularyByCategory("animals")
        val listAnimals = repository.generateInfoVocaList(getWordAnimals, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView
        val customVocabulary = CustomVocabulary(this, listAnimals)
        val gvAnimals = findViewById<GridView>(R.id.gvAnimals)
        gvAnimals.adapter = customVocabulary

        val imgPivAnimals = findViewById<ImageView>(R.id.imgPivAnimals)
        imgPivAnimals.setOnClickListener {
            finish()
        }
    }
}