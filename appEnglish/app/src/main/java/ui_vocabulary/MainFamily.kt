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

class MainFamily : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_family)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.aunt,
            R.drawable.peace,
            R.drawable.girl,
            R.drawable.father,
            R.drawable.grandfather,
            R.drawable.motherhood,
            R.drawable.happy,
            R.drawable.decoration,
            R.drawable.proud,
            R.drawable.man
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordFamily = repository.getVocabularyByCategory("family")
        val listFamily= repository.generateInfoVocaList(getWordFamily, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listFamily)
        val gvFamily= findViewById<GridView>(R.id.gvFamily)
        gvFamily.adapter = customVocabulary


        val imgPivFamily = findViewById<ImageView>(R.id.imgPivFamily)
        imgPivFamily.setOnClickListener {
            finish()
        }
    }
}