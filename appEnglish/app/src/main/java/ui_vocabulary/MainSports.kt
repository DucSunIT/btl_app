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

class MainSports : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_sports)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.badminton,
            R.drawable.baseball,
            R.drawable.boxing,
            R.drawable.cycling,
            R.drawable.football,
            R.drawable.golf,
            R.drawable.ball,
            R.drawable.swimming,
            R.drawable.tennis,
            R.drawable.volleyball
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordSports = repository.getVocabularyByCategory("sports")
        val listSports = repository.generateInfoVocaList(getWordSports, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listSports)
        val gvSports= findViewById<GridView>(R.id.gvSports)
        gvSports.adapter = customVocabulary


        val imgPivSports = findViewById<ImageView>(R.id.imgPivSports)
        imgPivSports.setOnClickListener {
            finish()
        }
    }
}