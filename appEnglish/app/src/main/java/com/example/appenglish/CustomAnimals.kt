package com.example.appenglish

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.IOException

class CustomAnimals(
    private val activity: Activity,
    private val list: List<Animals>,
    private val listSound: List<Int>
) : ArrayAdapter<Animals>(activity, R.layout.activity_animals) {

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
        /*chuyển file xml thành view*/
        val contexts = activity.layoutInflater
        val rowView = contexts.inflate(R.layout.custom_animals, parent, false)

        /*link các biến đến id của view trong file xml*/
        val imgWords = rowView.findViewById<ImageView>(R.id.imgWordCustomAnimals)
        val ipaWord = rowView.findViewById<TextView>(R.id.txtIpaWord)
        val word = rowView.findViewById<TextView>(R.id.txtWord)
        val imgDetails = rowView.findViewById<ImageView>(R.id.imgDetailsCustomAnimals)
        val imgSound = rowView.findViewById<ImageView>(R.id.imgSoundCustomAnimals)

        /*gán các dữ liệu cho các thành phần trong view*/
        imgWords.setImageResource(list[position].imageWord)
        ipaWord.text = list[position].ipaWord
        word.text = list[position].word
        imgDetails.setImageResource(list[position].imageDetails)
        imgSound.setImageResource(list[position].imageSound)

        /*xử lý đọc âm thanh của từ khi người dùng click vào biểu tượng loa*/
        imgSound.setOnClickListener {
            // Địa chỉ URL của âm thanh
            val audioUrl = "http://146.190.96.102/1.mp3"

            // Tạo MediaPlayer mới để phát âm thanh từ URL
            val mediaPlayer = MediaPlayer()

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

        /*xử lí chi tiết của từ -> đang phát triển*/
        imgDetails.setOnClickListener {
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
        return rowView

    }
}