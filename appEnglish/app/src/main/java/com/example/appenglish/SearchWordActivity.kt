package com.example.appenglish

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableIntListOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SearchWordActivity : AppCompatActivity() {

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_word)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val list = mutableListOf<SaveDetailWord>()
//        val intentGetData = intent
//        val bundleGetData = intentGetData.extras
//        if (bundleGetData != null) {
////            var getWord = bundleGetData.getString("WORD")
////            var getIPA = bundleGetData.getString("IPA")
////            var getType = bundleGetData.getString("TYPE")
////            var getDefinition = bundleGetData.getString("DEFINITION")
////            Log.d("WORD", "$getWord")
////            list.add("$getWord $getIPA \n $getType \n $getDefinition")
//            val getList = bundleGetData.getParcelableArrayList("LIST",)
//            if(getList!=null){
//                for(item in list){
//                    list.addAll(item)
//                }
//            }
//            Log.d("LIST", "${list.size}")
//            Log.d(
//                "BUNDLE_CONTENTS",
//                "Word: ${bundleGetData.getString("WORD")}, IPA: ${bundleGetData.getString("IPA")}"
//            )
//
//            val adt = ArrayAdapter(
//                this, android.R.layout.simple_list_item_1, list
//            )
//            val lv = findViewById<ListView>(R.id.lvDetailWord)
//            lv.adapter = adt
//
//        }


        // Nhận danh sách từ Intent
        val list = intent.getParcelableArrayListExtra<SaveDetailWord>("LIST")
        if (list != null) {
            Log.d("LIST", "Received list size: ${list.size}")
            for (item in list) {
                Log.d("ITEM", "Word: ${item.word}, IPA: ${item.ipa}, Type: ${item.type}, Definition: ${item.defi}")
            }

            // Hiển thị danh sách lên ListView
            val adt = MainDetailWord(this, list) // Sử dụng adapter tùy chỉnh
            val lv = findViewById<ListView>(R.id.lvDetailWord)
            lv.adapter = adt
        } else {
            Log.d("LIST", "No data received!")
        }

        previousActivity()
    }

    /*Hàm có chức năng xử lý sự kiện khi click vào mũi tên quay lại
    * ở SearchWordActivity*/
    private fun previousActivity() {
        val imgPivSearch = findViewById<ImageView>(R.id.imgPivSearchWord)
        imgPivSearch.setOnClickListener {
            finish()
        }
    }


}