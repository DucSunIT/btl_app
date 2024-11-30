package com.example.appenglish

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainTravel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_travel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.backpacker,
            R.drawable.car,
            R.drawable.gps,
            R.drawable.hotel,
            R.drawable.luggage,
            R.drawable.passport,
            R.drawable.schedule,
            R.drawable.world,
            R.drawable.travel,
            R.drawable.summer_break
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordTravel = repository.getVocabularyByCategory("travel")
        val listTravel = repository.generateInfoVocaList(getWordTravel, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listTravel)
        val gvToys = findViewById<GridView>(R.id.gvTravel)
        gvToys.adapter = customVocabulary


        val imgPivTravel = findViewById<ImageView>(R.id.imgPivTravel)
        imgPivTravel.setOnClickListener {
            finish()
        }
    }
}