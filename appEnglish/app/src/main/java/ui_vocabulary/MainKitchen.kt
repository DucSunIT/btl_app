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

class MainKitchen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_kitchen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgList = listOf(
            R.drawable.bowls,
            R.drawable.spoon_fork,
            R.drawable.knife,
            R.drawable.microwave,
            R.drawable.blender,
            R.drawable.oven,
            R.drawable.pan,
            R.drawable.plates,
            R.drawable.spoon,
            R.drawable.stove
        )

        val helper = DatabaseHelper(this)
        helper.openDatabase()

        val repository = VocabularyRepository(helper)
        val getWordKitchen = repository.getVocabularyByCategory("kitchen")
        val listKitchen = repository.generateInfoVocaList(getWordKitchen, imgList)

        // Thêm các đối tượng vào danh sách và vẽ lên GridView

        val customVocabulary = CustomVocabulary(this, listKitchen)
        val gvKitchen= findViewById<GridView>(R.id.gvKitchen)
        gvKitchen.adapter = customVocabulary


        val imgPivTravel = findViewById<ImageView>(R.id.imgPivKitchen)
        imgPivTravel.setOnClickListener {
            finish()
        }
    }
}