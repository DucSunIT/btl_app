package com.example.appenglish

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class CustomCursorAdapter(context: Context, cursor: Cursor?) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        // Inflate layout cho mỗi item trong danh sách
        return LayoutInflater.from(context).inflate(R.layout.custom_sugesstions, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        // Lấy dữ liệu từ Cursor
        val wordIndex = cursor.getColumnIndex("word")
        val ipaIndex = cursor.getColumnIndex("ipa")
        val typeIndex = cursor.getColumnIndex("type")

        val word = if (wordIndex != -1) cursor.getString(wordIndex) else "N/A"
        val ipa = if (ipaIndex != -1) cursor.getString(ipaIndex) else "N/A"
        val type = if (typeIndex != -1) cursor.getString(typeIndex) else "N/A"

        // Gán dữ liệu vào các TextView
        val wordTextView = view.findViewById<TextView>(R.id.txtWordSuggest)
        val ipaTextView = view.findViewById<TextView>(R.id.txtIPASuggest)
        val typeTextView = view.findViewById<TextView>(R.id.txtTypeSuggest)

        wordTextView?.text = word
        ipaTextView?.text = ipa
        typeTextView?.text = type
    }
}
