package com.example.appenglish

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.IOException

class MainDetailWord(private var context: Context, var wordList: List<SaveDetailWord>) :
    ArrayAdapter<SaveDetailWord>(context, R.layout.activity_custom_detail_word) {

    override fun getCount(): Int {
        return wordList.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.custom_detail_words, parent, false)

        val wordC = rowView.findViewById<TextView>(R.id.txtWord)
        val ipaC = rowView.findViewById<TextView>(R.id.txtIPA)
        val imgC = rowView.findViewById<ImageView>(R.id.imgSound)
        val typeC = rowView.findViewById<TextView>(R.id.txtType)
        val defiC = rowView.findViewById<TextView>(R.id.txtDefinition)

        wordC.text = wordList[position].word
        ipaC.text = wordList[position].ipa
        imgC.setImageResource(wordList[position].img)
        typeC.text = wordList[position].type
        defiC.text = wordList[position].defi

        imgC.setOnClickListener {
            playSound(wordList[position].word)
        }

        // Đảm bảo không lan sự kiện click từ toàn bộ item
        rowView.setOnClickListener {
            // Không làm gì khi click vào item chính
        }
        return rowView
    }

    private  fun playSound(word: String) {
        /*xử lý đọc âm thanh của từ khi người dùng click vào biểu tượng loa*/
        var id = 1
        val helper = DatabaseHelper(context)
        helper.openDatabase()
        val db = helper.readableDatabase
        val query = "select id from dictionary where word = ? "
        val res = db.rawQuery(query, arrayOf(word), null)
        if (res.moveToFirst()) {
            id = res.getInt(0)
            Log.d("IDPLAY", "$id")
        }
        res.close()
        val mediaPlayer = MediaPlayer()
        val audioUrl = "http://178.128.52.124/audio/$id.mp3"
        try {
            // Đặt nguồn âm thanh từ URL
            mediaPlayer.setDataSource(audioUrl)

            // Chuẩn bị âm thanh không làm gián đoạn UI
            mediaPlayer.prepareAsync()

            // Khi âm thanh đã sẵn sàng, bắt đầu phát
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()  // Phát âm thanh
            }

            // Xử lý khi có lỗi xảy ra trong quá trình phát
            mediaPlayer.setOnErrorListener { _, _, _ ->
                Toast.makeText(context, "Lỗi khi phát âm thanh", Toast.LENGTH_SHORT).show()
                true  // Trả về true để chỉ ra lỗi đã được xử lý
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Lỗi khi tải âm thanh", Toast.LENGTH_SHORT).show()
        }
    }

}