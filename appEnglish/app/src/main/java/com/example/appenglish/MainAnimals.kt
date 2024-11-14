package com.example.appenglish

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
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

        val listAnimals = mutableListOf<Animals>()
//        listAnimals.add(Animals(R.drawable.lion, "/ˈlaɪən/", "Lion", R.drawable.details, R.drawable.loa))
        val imgList = listOf(
            R.drawable.lion,
            R.drawable.cat,
            R.drawable.chicken,
            R.drawable.bear,
            R.drawable.fox
        )
        val ipaList = listOf("/ˈlaɪən/", "/kæt/", "/ˈʧɪkɪn/", "/beə/", "/fɒks/")
        val wordList = listOf("Lion", "Cat", "Chicken", "Bear", "Fox")
        val soundList = listOf(R.raw.word1, R.raw.word2, R.raw.word3, R.raw.word4, R.raw.word5)

        val minSize = minOf(imgList.size, wordList.size, ipaList.size)
        // 0 <=n < size
        for (item in 0 until minSize) {
            listAnimals.add(
                Animals(
                    imgList[item],
                    ipaList[item],
                    wordList[item],
                    R.drawable.details,
                    R.drawable.loa
                )
            )
        }

        val customAnimals = CustomAnimals(this, listAnimals, soundList)
        val gvAnimals = findViewById<GridView>(R.id.gvAnimals)
        gvAnimals.adapter = customAnimals

        val imgPivAnimals = findViewById<ImageView>(R.id.imgPivAnimals)
        imgPivAnimals.setOnClickListener {
            finish()
        }
    }
}