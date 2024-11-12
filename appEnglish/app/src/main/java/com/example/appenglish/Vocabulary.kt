package com.example.appenglish

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Vocabulary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vocabulary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addEvents()
    }

    private fun addEvents() {
        drawDataVoca()
        handlePiv()
    }

    private fun drawDataVoca() {
        val titleSubject = listOf(
            "Động vật",
            "Hoa quả",
            "Đồ chơi",
            "Du lịch",
            "Gia đình",
            "Nhà bếp",
            "Thể thao",
            "Số đếm"
        )
        val imagesSubject = listOf(
            R.drawable.lion,
            R.drawable.group,
            R.drawable.flat,
            R.drawable.airplane,
            R.drawable.family,
            R.drawable.kitchen,
            R.drawable.sport,
            R.drawable.numbers
        )
        val listSubject = mutableListOf<OutData>()

        for (i in 0.. titleSubject.lastIndex) if (i< imagesSubject.size) {
            listSubject.add(OutData(imagesSubject[i], titleSubject[i]))
        }
        val gvVocabulary = findViewById<GridView>(R.id.gvMain)
        val resVocabulary = CustomGridVoca(this, listSubject)
        gvVocabulary.adapter = resVocabulary
    }

    // hanlde imgePiv

    private fun handlePiv(){
        val btnPiv = findViewById<ImageView>(R.id.imgPiv)
        btnPiv.setOnClickListener {
//            val intentPiv = Intent(this, MainActivity::class.java)
//            startActivity(intentPiv)
            finish()
        }
    }

}