package com.example.appenglish

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAnimals(
    private val activity: Activity,
    private val list: List<Animals>,
    private val listSound: List<Int>
): ArrayAdapter<Animals>(activity, R.layout.activity_animals) {

    override fun getCount(): Int {
        return list.size
    }

//    override fun getItem(position: Int): Any {
//        return list[position]
//    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexts = activity.layoutInflater
        val rowView = contexts.inflate(R.layout.custom_animals, parent, false)

        val imgWords = rowView.findViewById<ImageView>(R.id.imgWordCustomAnimals)
        val ipaWord = rowView.findViewById<TextView>(R.id.txtIpaWord)
        val word = rowView.findViewById<TextView>(R.id.txtWord)
        val imgDetails = rowView.findViewById<ImageView>(R.id.imgDetailsCustomAnimals)
        val imgSound = rowView.findViewById<ImageView>(R.id.imgSoundCustomAnimals)


        imgWords.setImageResource(list[position].imageWord)
        ipaWord.text = list[position].ipaWord
        word.text = list[position].word
        imgDetails.setImageResource(list[position].imageDetails)
        imgSound.setImageResource(list[position].imageSound)


        imgSound.setOnClickListener {
            // Dừng và giải phóng MediaPlayer trước khi tạo lại để tránh trùng lặp âm thanh
            val mediaPlayer = MediaPlayer.create(activity, listSound[position])
            mediaPlayer.start()
        }

        return rowView

    }
}