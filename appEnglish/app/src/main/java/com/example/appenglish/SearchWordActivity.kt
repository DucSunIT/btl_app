package com.example.appenglish

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data_class.SaveDetailWord
import database.DatabaseHelper

class SearchWordActivity : AppCompatActivity() {
    private lateinit var popupWindow: PopupWindow
    private val wordList = mutableListOf<String>() // Gợi ý từ
    private lateinit var adt: MainDetailWord // Adapter cho ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_word)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Nhận danh sách từ Intent và hiển thị trong ListView
        val list = intent.getParcelableArrayListExtra<SaveDetailWord>("LIST")
        if (list != null) {
            adt = MainDetailWord(this, list)
            val lv = findViewById<ListView>(R.id.lvDetailWord)
            lv.adapter = adt
        }

        // Gọi hàm xử lý các chức năng
        popupSuggest()
        previousActivity()
    }

    private fun previousActivity() {
        val imgPivSearch = findViewById<ImageView>(R.id.imgPivSearchWord)
        imgPivSearch.setOnClickListener { finish() }
    }

    @SuppressLint("InflateParams")
    private fun popupSuggest() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_suggest, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            800
        )
        popupWindow.isFocusable = false
        popupWindow.isOutsideTouchable = true

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rvSuggest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(wordList) { selectedItem ->
            searchView.setQuery(selectedItem, false)
            popupWindow.dismiss()
            updateListView(selectedItem)
        }
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.custom_line_suggest)!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showPopupWindowIfNecessary(searchView)
            } else {
                popupWindow.dismiss()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    updateListView(query)
                }
                popupWindow.dismiss()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    updateSuggestions(newText, adapter)
                    showPopupWindowIfNecessary(searchView)
                } else {
                    popupWindow.dismiss()
                }
                return true
            }
        })
    }

    private fun showPopupWindowIfNecessary(searchView: SearchView) {
        // Kiểm tra Activity có còn hợp lệ không
        if (isFinishing || isDestroyed) {
            return
        }

        // Trì hoãn việc hiển thị PopupWindow để đảm bảo Activity đã khởi tạo xong
        searchView.post {
            try {
                if (!popupWindow.isShowing) {
                    popupWindow.showAsDropDown(searchView)
                }
            } catch (e: WindowManager.BadTokenException) {
                Log.e("PopupWindow", "Activity not in valid state: ${e.message}")
            }
        }
    }

    private fun updateSuggestions(newText: String, adapter: RvAdapter) {
        val query = "$newText%"
        val helper = DatabaseHelper(applicationContext)
        helper.openDatabase()
        val db = helper.readableDatabase
        wordList.clear()

        val res = db.rawQuery(
            "SELECT * FROM dictionary WHERE word LIKE ? LIMIT 50",
            arrayOf(query)
        )

        if (res.moveToFirst()) {
            do {
                // Kiểm tra giá trị NULL và thay thế bằng giá trị mặc định nếu cần
                val word = res.getString(res.getColumnIndexOrThrow("word")) ?: "Unknown"
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa")) ?: "Unknown IPA"
                val type = res.getString(res.getColumnIndexOrThrow("type")) ?: "Unknown Type"
                // Thêm từ vào danh sách
                wordList.add("$word \n/$ipa/ \n$type")
            } while (res.moveToNext())
        }
        res.close()

        // Kiểm tra danh sách từ và cập nhật giao diện
        if (wordList.isEmpty()) {
            if (::popupWindow.isInitialized) { // Kiểm tra popupWindow đã được khởi tạo
                popupWindow.dismiss()
            }
        } else {
            adapter.updateData(wordList)
            if (::popupWindow.isInitialized && !popupWindow.isShowing) {
                showPopupWindowIfNecessary(findViewById(R.id.searchView))
            }
        }
    }


    private fun updateListView(query: String) {
        val helper = DatabaseHelper(applicationContext)
        helper.openDatabase()
        val db = helper.readableDatabase
        val list = ArrayList<SaveDetailWord>()

        val res = db.rawQuery(
            "SELECT * FROM dictionary WHERE word = ?",
            arrayOf(query)
        )
        if (res.moveToFirst()) {
            do {
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                val type = res.getString(res.getColumnIndexOrThrow("type"))
                val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                list.add(
                    SaveDetailWord(
                        word ?: "Unknown",
                        "[${ipa ?: "Unknown IPA"}]",
                        R.drawable.loa,
                        type ?: "Unknown Type",
                        definition ?: "No definition"
                    )
                )
                val contentValues = ContentValues().apply {
                    put("word", word)
                    put("ipa", ipa)
                    put("type", type)
                    put("definition", definition)
                }
                val checkCursor = db.rawQuery(
                    "SELECT * FROM recent_word WHERE word = ?",
                    arrayOf(word)
                )
                if (checkCursor.count == 0) {
                    val result = db.insert("recent_word", null, contentValues)
                    if (result != -1L) {
                        Log.d("INSERT", "Inserted word '$word'")
                    } else {
                        Log.e("INSERT", "Failed to insert word '$word'")
                    }
                } else {
                    Log.d("INSERT", "Word '$word' already exists in recent_word")
                }
                checkCursor.close()
            } while (res.moveToNext())
        }
        res.close()

        adt.updateData(list)
        adt.notifyDataSetChanged()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(findViewById<View>(R.id.searchView).windowToken, 0)
    }

}
