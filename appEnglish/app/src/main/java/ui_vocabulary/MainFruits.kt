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

class MainFruits : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fruits)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgList = listOf(
            R.drawable.apple,
            R.drawable.cherry,
            R.drawable.durian,
            R.drawable.grape_fruit,
            R.drawable.guava,
            R.drawable.lemon,
            R.drawable.mango,
            R.drawable.orange,
            R.drawable.peach,
            R.drawable.pear
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordFruits = repository.getVocabularyByCategory("fruit")
        val listFruits = repository.generateInfoVocaList(getWordFruits, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listFruits)
        val gvFruits = findViewById<GridView>(R.id.gvFruits)
        gvFruits.adapter = customVocabulary

        val imgPivFruits = findViewById<ImageView>(R.id.imgPivFruits)
        imgPivFruits.setOnClickListener {
            finish()
        }
    }
}