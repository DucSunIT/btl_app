package com.example.appenglish

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.media.MediaPlayer
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appenglish.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // create viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // call fun
        addEvents()
    }

    private fun addEvents() {
//        handleHome()
//        handleSearch()
//        handleDictionary()
//        handleSetting()
//        handleExit()
        handleLayoutSearch()
        handleLayoutVocabulary()
        handleClickLetGo()
        handleClickProfile()
    }

    private fun handleClickProfile() {
        binding.imgProfile.setOnClickListener {
            val intentProfile = Intent(this, LoginActivity::class.java)
            startActivity(intentProfile)
        }
    }

    private fun handleClickLetGo() {

    }

//    private fun handleExit() {
//        binding.imgExit.setOnClickListener {
//            Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//    }

    private fun handleLayoutVocabulary() {
        binding.layoutDictonary.setOnClickListener {
            val intentVocabulary = Intent(this, Vocabulary::class.java)
            startActivity(intentVocabulary)
        }
    }



    private fun handleLayoutSearch() {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val intentSearch = Intent(this, SearchWordEnglish::class.java)
                startActivity(intentSearch)
                true
            } else {
                false
            }
        }
    }

//    private fun handleSetting() {
//        binding.imgSetting.setOnClickListener {
//            Toast.makeText(this, "View Setting", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun handleDictionary() {
//        binding.imgDictionary.setOnClickListener {
//            Toast.makeText(this, "View Dictionary", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun handleSearch() {
//        binding.imgSearch.setOnClickListener {
//            binding.edtSearch.requestFocus()
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(binding.imgSearch, InputMethodManager.SHOW_IMPLICIT)
//        }
//    }

//    private fun handleHome() {
//        binding.imgHome.setOnClickListener {
//            Toast.makeText(this, "View Home", Toast.LENGTH_SHORT).show()
//        }
//    }


}