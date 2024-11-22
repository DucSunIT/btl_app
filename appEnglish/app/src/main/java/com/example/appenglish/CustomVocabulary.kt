package com.example.appenglish

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.IOException

class CustomVocabulary(
    private val activity: Activity,
    private val list: List<InfoVoca>
) : ArrayAdapter<Vocabulary>(activity, R.layout.activity_voca) {

    override fun getCount(): Int {
        return list.size
    }


    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        /*chuyển file xml thành view*/
        val contexts = activity.layoutInflater
        val rowView = contexts.inflate(R.layout.custom_detail_voca, parent, false)

        /*link các biến đến id của view trong file xml*/
        val imgWords = rowView.findViewById<ImageView>(R.id.imgDetailLargeVoca)
        val ipaWord = rowView.findViewById<TextView>(R.id.txtIpaDetailVoca)
        val word = rowView.findViewById<TextView>(R.id.txtWordDetailVoca)
        val imgDetails = rowView.findViewById<ImageView>(R.id.imgDetailVoca)
        val imgSound = rowView.findViewById<ImageView>(R.id.imgSoundDetailVoca)

        /*gán các dữ liệu cho các thành phần trong view*/
        imgWords.setImageResource(list[position].imgRes)
        ipaWord.text = list[position].ipa
        word.text = list[position].word
        imgDetails.setImageResource(list[position].imageDetails)
        imgSound.setImageResource(list[position].imageSound)

        imgSound.setOnClickListener {
            playSound(list[position].word)

        }
        /*xử lí chi tiết của từ -> đang phát triển*/
        imgDetails.setOnClickListener {
//            viewDetailWord(word)
        }
        return rowView
    }

    private fun viewDetailWord(word: String) {
        val dialogDetails = AlertDialog.Builder(activity)
        dialogDetails.apply {
            // tiêu đề của dialog
            setTitle("Thông báo")
            // Nội dung của dialog
            setMessage("Chức năng đang phát triển !")
            /*Nếu người dùng bấm OK dialog sẽ ẩn đi*/
            setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
        }
        dialogDetails.show()
    }

    fun playSound(word: String) {
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
        val audioUrl = "http://146.190.96.102/$id.mp3"
        Log.d("IDPLAY", "$id")
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