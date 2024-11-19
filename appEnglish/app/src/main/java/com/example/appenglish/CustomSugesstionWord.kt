package com.example.appenglish

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.w3c.dom.Text

class CustomSugesstionWord(
    private val activity: Activity,
    private val wordList: List<SugesstionsWord>
) : ArrayAdapter<SugesstionsWord>(activity, R.layout.custom_sugesstions) {
    override fun getCount(): Int {
        return wordList.size

    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: activity.layoutInflater.inflate(R.layout.custom_sugesstions, parent, false)

        val cword = rowView.findViewById<TextView>(R.id.txtWordSuggest)
        val cipa = rowView.findViewById<TextView>(R.id.txtIPASuggest)
        val ctype = rowView.findViewById<TextView>(R.id.txtTypeSuggest)

        val suggestionWord = wordList[position]
        cword.text = suggestionWord.word
        cipa.text = suggestionWord.ipa
        ctype.text = suggestionWord.type

        return rowView
    }


}