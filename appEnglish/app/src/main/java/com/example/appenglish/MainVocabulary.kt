package com.example.appenglish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
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
        val helper = DatabaseHelper(this)
        val db = helper.readableDatabase
        val res = db.rawQuery("select * from vocabulary", null)
        val vocabList = mutableListOf<String>()
        if (res.moveToFirst()) {
            do {
                val name = res.getString(res.getColumnIndexOrThrow("namevoca"))
                vocabList.add(name)
                Log.d("NAME", name)
            } while (res.moveToNext())
        }
        res.close()

        val imagesSubject = listOf(
            R.drawable.lion,
            R.drawable.group,
            R.drawable.flat,
            R.drawable.airplane,
            R.drawable.family,
            R.drawable.kitchen,
            R.drawable.sport,
            R.drawable.numbers,
            R.drawable.mood
        )
        val listSubject = mutableListOf<Vocabulary>()

        for (i in 0..vocabList.lastIndex) if (i < imagesSubject.size) {
            listSubject.add(Vocabulary(imagesSubject[i], vocabList[i]))
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
                    1 -> {
                        val clickItemFruits = Intent(this, MainFruits::class.java)
                        startActivity(clickItemFruits)
                    }
                    2 -> {
                        val clickItemToys = Intent(this, MainToys::class.java)
                        startActivity(clickItemToys)
                    }
                    3 -> {

                    }


                }
            }
    }

    // handle imgPiv
    private fun handlePivVocabulary() {
        val btnPiv = findViewById<ImageView>(R.id.imgPiv)
        btnPiv.setOnClickListener {
            finish()
        }
    }

}