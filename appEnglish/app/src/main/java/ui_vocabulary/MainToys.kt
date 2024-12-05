package ui_vocabulary

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appenglish.R
import custom_ui.CustomVocabulary
import database.DatabaseHelper

class MainToys : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_toy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.bong,
            R.drawable.road_trip,
            R.drawable.doll,
            R.drawable.kites,
            R.drawable.ghep_hinh,
            R.drawable.robot,
            R.drawable.scooter,
            R.drawable.xich_du,
            R.drawable.teddy_bear,
            R.drawable.tauhoa
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordToys = repository.getVocabularyByCategory("toys")
        val listToys = repository.generateInfoVocaList(getWordToys, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listToys)
        val gvToys = findViewById<GridView>(R.id.gvToys)
        gvToys.adapter = customVocabulary


        val imgPivToys = findViewById<ImageView>(R.id.imgPivToys)
        imgPivToys.setOnClickListener {
            finish()
        }
    }
}