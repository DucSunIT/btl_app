package custom_ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import data_class.InfoVoca
import com.example.appenglish.R
import data_class.Vocabulary
import database.DatabaseHelper
import java.io.IOException

class CustomVocabulary(
    private val activity: Activity,
    private val list: List<InfoVoca>,

    ) : ArrayAdapter<Vocabulary>(activity, R.layout.activity_voca) {
    lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    val viewDialog = LayoutInflater.from(activity).inflate(R.layout.custom_dialog, null)
    override fun getCount(): Int {
        return list.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // chuyển file xml thành view
        val contexts = activity.layoutInflater
        val rowView = contexts.inflate(R.layout.custom_detail_voca, parent, false)

        // link các biến đến id của view trong file xml
        val imgWords = rowView.findViewById<ImageView>(R.id.imgDetailLargeVoca)
        val ipaWord = rowView.findViewById<TextView>(R.id.txtIpaDetailVoca)
        val word = rowView.findViewById<TextView>(R.id.txtWordDetailVoca)
        val imgDetails = rowView.findViewById<ImageView>(R.id.imgDetailVoca)
        val imgSound = rowView.findViewById<ImageView>(R.id.imgSoundVoca)

        // gán các dữ liệu cho các thành phần trong view
        imgWords.setImageResource(list[position].imgRes)
        ipaWord.text = list[position].ipa
        word.text = list[position].word
        imgDetails.setImageResource(list[position].imageDetails)
        imgSound.setImageResource(list[position].imageSound)

        imgSound.setOnClickListener {
            playSound(list[position].word)

        }
        // xử lí chi tiết của từ
        imgDetails.setOnClickListener {
            viewDetailWord(list[position].word)
            clickSound(list[position].word)
        }
        return rowView
    }

    @SuppressLint("Recycle")
    private fun viewDetailWord(word: String) {
        // Truy vấn cơ sở dữ liệu chỉ cho từ hiện tại
        val helper = DatabaseHelper(context)
        helper.openDatabase()
        val db = helper.readableDatabase
        val query = "SELECT * FROM dictionary WHERE word = ?"
        val res = db.rawQuery(query, arrayOf(word), null)

        if (res.moveToFirst()) {
            val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
            val word = res.getString(res.getColumnIndexOrThrow("word"))
            val meaning = res.getString(res.getColumnIndexOrThrow("meaning_word"))
            val example = res.getString(res.getColumnIndexOrThrow("example"))
            val exampleTranslate = res.getString(res.getColumnIndexOrThrow("example_translate"))
            val wordToImageMap = mapOf(
                // animals
                "bear" to R.drawable.bear,
                "cat" to R.drawable.cat,
                "elephant" to R.drawable.voi,
                "fox" to R.drawable.fox,
                "giraffe" to R.drawable.huou,
                "hippopotamus" to R.drawable.hama,
                "lion" to R.drawable.lion,
                "octopus" to R.drawable.bachtuoc,
                "porcupine" to R.drawable.nhim,
                "squirrel" to R.drawable.soc,

                // fruits
                "apple" to R.drawable.apple,
                "cherry" to R.drawable.cherry,
                "durian" to R.drawable.durian,
                "grape" to R.drawable.grape_fruit,
                "guava" to R.drawable.guava,
                "lemon" to R.drawable.lemon,
                "mango" to R.drawable.mango,
                "orange" to R.drawable.orange,
                "peach" to R.drawable.peach,
                "pear" to R.drawable.pear,

                // toys
                "ball" to R.drawable.bong,
                "car" to R.drawable.road_trip,
                "doll" to R.drawable.doll,
                "kite" to R.drawable.kites,
                "puzzle" to R.drawable.ghep_hinh,
                "robot" to R.drawable.robot,
                "scooter" to R.drawable.scooter,
                "swing" to R.drawable.xich_du,
                "teddy" to R.drawable.teddy_bear,
                "train" to R.drawable.tauhoa,

                // travels
                "adventure" to R.drawable.backpacker,
                "cabin" to R.drawable.car,
                "destination" to R.drawable.gps,
                "hotel" to R.drawable.hotel,
                "luggage" to R.drawable.luggage,
                "passport" to R.drawable.passport,
                "schedule" to R.drawable.schedule,
                "tour" to R.drawable.world,
                "tourist" to R.drawable.travel,
                "vacation" to R.drawable.summer_break,

                // family
                "aunt" to R.drawable.aunt,
                "brother" to R.drawable.peace,
                "daughter" to R.drawable.girl,
                "father" to R.drawable.father,
                "grandfather" to R.drawable.grandfather,
                "grandmother" to R.drawable.motherhood,
                "mother" to R.drawable.happy,
                "sister" to R.drawable.decoration,
                "son" to R.drawable.proud,
                "uncle" to R.drawable.man,

                // kitchen
                "bowl" to R.drawable.bowls,
                "fork" to R.drawable.spoon_fork,
                "knife" to R.drawable.knife,
                "microwave" to R.drawable.microwave,
                "mixer" to R.drawable.blender,
                "oven" to R.drawable.oven,
                "pan" to R.drawable.pan,
                "plate" to R.drawable.plates,
                "spoon" to R.drawable.spoon,
                "gas stove" to R.drawable.stove,

                //sports
                "badminton" to R.drawable.badminton,
                "baseball" to R.drawable.baseball,
                "boxing" to R.drawable.boxing,
                "cycling" to R.drawable.cycling,
                "football" to R.drawable.football,
                "golf" to R.drawable.golf,
                "rugby" to R.drawable.ball,
                "swimming" to R.drawable.swimming,
                "tennis" to R.drawable.tennis,
                "volleyball" to R.drawable.volleyball,

                // numbers
                "eight" to R.drawable.eight,
                "five" to R.drawable.five,
                "four" to R.drawable.four,
                "nine" to R.drawable.nine,
                "one" to R.drawable.one,
                "seven" to R.drawable.seven,
                "six" to R.drawable.six,
                "ten" to R.drawable.ten,
                "three" to R.drawable.three,
                "two" to R.drawable.two
            )
            val imageRes = wordToImageMap[word] ?: R.drawable.close
            // Tạo đối tượng CustomDialog
            val customDialog =
                CustomDialog(imageRes, "/$ipa/", word, meaning, example, "($exampleTranslate)")

            // Gọi hàm để hiển thị dialog với thông tin này
            showDialog(customDialog)
        }
    }

    private fun showDialog(customDialog: CustomDialog) {
        val dialogBuilder = AlertDialog.Builder(activity, R.style.ThemeCustom)

        // Tạo các view để hiển thị dữ liệu từ CustomDialog
        val imgDetails = viewDialog.findViewById<ImageView>(R.id.imgDetails)
        val txtIpaDetails = viewDialog.findViewById<TextView>(R.id.txtIpaDetails)
        val txtWordDetails = viewDialog.findViewById<TextView>(R.id.txtWordDetails)
        val txtMeaning = viewDialog.findViewById<TextView>(R.id.txtMeaning)
        val txtExample = viewDialog.findViewById<TextView>(R.id.txtExample)
        val txtExampleTranslate = viewDialog.findViewById<TextView>(R.id.txtExampleTranslate)

        // Cập nhật thông tin cho các view từ đối tượng CustomDialog
        imgDetails.setImageResource(customDialog.img)
        txtIpaDetails.text = customDialog.ipaDetails
        txtWordDetails.text = customDialog.wordDetails
        txtMeaning.text = customDialog.meaningDetails
        txtExample.text = customDialog.exampleDetails
        txtExampleTranslate.text = customDialog.exampleTranslate

        // Thiết lập view cho AlertDialog
        val parent = viewDialog.parent as? ViewGroup
        parent?.removeView(viewDialog)
        dialogBuilder.setView(viewDialog)
        // Hiển thị dialog
        dialog = dialogBuilder.create()
        dialog.show()

        val btnClose = viewDialog.findViewById<ImageButton>(R.id.imgCloseDialog)
        btnClose.setOnClickListener { dialog.dismiss() }
    }

    private fun clickSound(word: String) {
        val imgSound = viewDialog.findViewById<ImageView>(R.id.imgSoundDetails)
        imgSound.setOnClickListener {
            playSound(word)
        }
    }

    private fun playSound(word: String) {
        // xử lý đọc âm thanh của từ khi người dùng click vào biểu tượng loa
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