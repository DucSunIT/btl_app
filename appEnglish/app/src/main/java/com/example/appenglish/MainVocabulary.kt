package com.example.appenglish

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainVocabulary : AppCompatActivity() {
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
        handlePivVocabulary()
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
        val listSubject = mutableListOf<Vocabulary>()

        for (i in 0..titleSubject.lastIndex) if (i < imagesSubject.size) {
            listSubject.add(Vocabulary(imagesSubject[i], titleSubject[i]))
        }
        val gvVocabulary = findViewById<GridView>(R.id.gvVocabulary)
        val resVocabulary = CustomGridVoca(this, listSubject)
        gvVocabulary.adapter = resVocabulary

        // handle click item
        gvVocabulary.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                when (position) {
                    0 -> {
                        val clickItemAnimals = Intent(this, MainAnimals::class.java)
                        startActivity(clickItemAnimals)
                    }
                }
            }
    }

    // handle imgPiv
    private fun handlePivVocabulary() {
        val btnPiv = findViewById<ImageView>(R.id.imgPiv)
        btnPiv.setOnClickListener {
//            val intentPiv = Intent(this, MainActivity::class.java)
//            startActivity(intentPiv)
            finish()
        }
    }

}