package com.example.appenglish

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.GridView

class VocabularyRepository(private val helper: DatabaseHelper) {
    fun getVocabularyByCategory(typeClass: String): List<InfoVocabulary> {
        val db = helper.readableDatabase
        val res = db.rawQuery("select * from dictionary where class = ? ", arrayOf(typeClass), null)
        val vocabList = mutableListOf<InfoVocabulary>()
        if (res.moveToFirst()) {
            do {
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                vocabList.add(InfoVocabulary("/$ipa/", word))
                Log.d("TEST", "$ipa, $word")
                Log.d("LIST", "${vocabList.size}")
            } while (res.moveToNext())
        }
        res.close()
        return vocabList
    }

    fun generateInfoVocaList(
        vocabularies: List<InfoVocabulary>,
        images: List<Int>
    ): List<InfoVoca> {
        val minSize = minOf(vocabularies.size, images.size)
        val uniqueVocaSet = mutableSetOf<InfoVoca>()

        for (i in 0 until minSize) {
            val ipa = vocabularies[i].ipa
            val word = vocabularies[i].name
            val imgRes = images[i]
            uniqueVocaSet.add(
                InfoVoca(
                    imgRes,
                    ipa,
                    word,
                    R.drawable.details,
                    R.drawable.loa
                )
            )
        }

        return uniqueVocaSet.toList()
    }
}