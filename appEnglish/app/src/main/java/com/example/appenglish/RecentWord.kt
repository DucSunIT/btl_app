package com.example.appenglish

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecentWord : AppCompatActivity() {
    private lateinit var adt: MainRecentWord
    private val list = ArrayList<SaveRecentWord>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recent_word)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val previous = findViewById<ImageView>(R.id.imgPivRecentWord)
        previous.setOnClickListener { finish() }

        // Khởi tạo danh sách
        list.addAll(getRecentWordsFromDatabase())

        // Thiết lập adapter
        val lvRecent = findViewById<ListView>(R.id.lvRecentWord)
        adt = MainRecentWord(this, list)
        lvRecent.adapter = adt
    }

    private fun getRecentWordsFromDatabase(): ArrayList<SaveRecentWord> {
        val helper = DatabaseHelper(this)
        helper.openDatabase()
        val db = helper.readableDatabase
        val list = ArrayList<SaveRecentWord>()

        val res = db.rawQuery("select * from recentword", null)
        if (res.moveToFirst()) {
            do {
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                val type = res.getString(res.getColumnIndexOrThrow("type"))
                val definition = res.getString(res.getColumnIndexOrThrow("definition"))

                list.add(
                    SaveRecentWord(
                        word ?: "Unknown",
                        "/${ipa ?: "Unknown IPA"}/",
                        R.drawable.loa,
                        R.drawable.delete,
                        type ?: "Unknown Type",
                        definition ?: "No definition"
                    )
                )
            } while (res.moveToNext())
        }
        res.close()
        return list
    }
}