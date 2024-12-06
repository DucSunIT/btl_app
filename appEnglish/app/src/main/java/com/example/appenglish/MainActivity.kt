package com.example.appenglish

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appenglish.databinding.ActivityMainBinding
import custom_ui.CustomListMember
import data_class.ListMember
import data_class.SaveDetailWord
import database.DatabaseHelper
import ui_vocabulary.MainVocabulary

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var helper: DatabaseHelper
    private val listMember = mutableListOf<ListMember>()
    private val wordList = mutableListOf<String>()
    private lateinit var popupWindow: PopupWindow
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
        addEvents()
    }

    private fun addEvents() {
        handleLayoutVocabulary()
        handleClickProfile()
        displayListUser()
        handleClickVip()
        handleClickRecentWord()
        handleLayoutHobbyWord()
        popupSuggest()
    }

    private fun handleLayoutHobbyWord() {
        binding.layoutHobbyWord.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Thông báo")

                setMessage("Chức năng đang phát triển")

                setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun handleClickRecentWord() {
        binding.layoutRecentWord.setOnClickListener {
            val intentRecentWord = Intent(this, RecentWord::class.java)
            startActivity(intentRecentWord)
        }

    }

    /*
    * Hàm có chức năng xử lý sự kiện khi người dụng click vào
    * hình ảnh VIP sẽ hiện ra một thông báo (AlertDialog)*/
    private fun handleClickVip() {
        binding.imgPremium.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                // tiêu đề dialog
                setTitle("Thông báo")
                // nội dung dialog
                setMessage("Chức năng đang phát triển !")
                // xử lí nút OK, người dùng bấm OK dialog sẽ ẩn đi
                setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            // hiển thị dialog
            dialog.show()
        }
    }

    /*Hàm có chức năng hiển thị các thành viên trong nhóm thông qua ListView*/
    private fun displayListUser() {
        /*Vẽ lên ListView sử dụng adapter, dữ liệu laasy từ csdl*/
        helper = DatabaseHelper(this)
        helper.openDatabase()
        handleDataMembers(helper)
        val customLV = CustomListMember(this, listMember)
        binding.lvMemberGroup.adapter = customLV


    }

    /*Hàm có chức năng xử lý sự kiện khi người dùng click vào hình ảnh
    * user trên màn hình, sẽ chuyển hướng người dùng sang LoginActivity*/
    private fun handleClickProfile() {
        binding.imgProfile.setOnClickListener {
            val intentProfile = Intent(this, LoginActivity::class.java)
            startActivity(intentProfile)
        }
    }

    /*Hàm có chức năng xử lý sự kiện khi người dùng click vào layout từ vựng
     * trên màn hình, sẽ chuyển hướng người dùng sang MainVocabulary*/
    private fun handleLayoutVocabulary() {
        binding.layoutDictonary.setOnClickListener {
            val intentMainVocabulary = Intent(this, MainVocabulary::class.java)
            startActivity(intentMainVocabulary)
        }
    }

    /*Hàm có chức năng truy vấn csdl và đẩy dữ liệu vào List database*/
    @SuppressLint("Recycle")
    private fun handleDataMembers(helper: DatabaseHelper) {
        val db = helper.readableDatabase
        val res = db.rawQuery("SELECT FULLNAME FROM MEMBERS", null)
        if (res.moveToFirst()) {
            listMember.clear()
            do {
                val fullName = res.getString(0)
                listMember.add(ListMember(R.drawable.profile, fullName, R.drawable.star_vip))
            } while (res.moveToNext())
        }
        res.close()
    }

    private fun popupSuggest() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_suggest, null)
        popupWindow = setupPopupWindow(popupView)
        setupRecyclerView(popupView)
        setupSearchViewListeners()
    }

    private fun setupPopupWindow(popupView: View): PopupWindow {
        return PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            800
        ).apply {
            isFocusable = false
            isOutsideTouchable = true
        }
    }

    private fun setupRecyclerView(popupView: View) {
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rvSuggest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(wordList) { selectedItem ->
            onWordSelected(selectedItem)
        }
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        ).apply {
            setDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.custom_line_suggest
                )!!
            )
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun onWordSelected(selectedItem: String) {
        binding.searchView.setQuery(selectedItem, false)
        popupWindow.dismiss()
        startSearchWordActivity(selectedItem)
    }

    private fun startSearchWordActivity(query: String) {
        val intent = Intent(this, SearchWordActivity::class.java)
        val bundle = Bundle()
        val list = fetchWordDetailsFromDatabase(query)
        if (list.isNotEmpty()) {
            bundle.putParcelableArrayList("LIST", list)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun fetchWordDetailsFromDatabase(query: String): ArrayList<SaveDetailWord> {
        val helper = DatabaseHelper(applicationContext)
        helper.openDatabase()
        val db = helper.readableDatabase
        val list = ArrayList<SaveDetailWord>()
        val res =
            db.rawQuery("SELECT * FROM dictionary WHERE word = ?", arrayOf(query), null)
        if (res.moveToFirst()) {
            do {
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                val type = res.getString(res.getColumnIndexOrThrow("type"))
                val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                list.add(
                    SaveDetailWord(
                        word ?: "Unknown",
                        "/${ipa ?: "Unknown IPA"}/",
                        R.drawable.loa,
                        type ?: "Unknown Type",
                        definition ?: "No definition"
                    )
                )
                insertRecentWord(db, word, ipa, type, definition)
            } while (res.moveToNext())
        }
        res.close()
        return list
    }

    private fun insertRecentWord(
        db: SQLiteDatabase,
        word: String?,
        ipa: String?,
        type: String?,
        definition: String?
    ) {
        val contentValues = ContentValues().apply {
            put("word", word)
            put("ipa", ipa)
            put("type", type)
            put("definition", definition)
        }
        val checkCursor = db.rawQuery("SELECT * FROM recent_word WHERE word = ?", arrayOf(word))
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
    }

    private fun setupSearchViewListeners() {
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                popupWindow.showAsDropDown(binding.searchView)
            } else {
                popupWindow.dismiss()
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleQuerySubmit(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleQueryChange(newText)
                return true
            }
        })
    }

    private fun handleQuerySubmit(query: String?) {
        popupWindow.dismiss()
        if (!query.isNullOrEmpty()) {
            Toast.makeText(this, "Search $query", Toast.LENGTH_SHORT).show()
            startSearchWordActivity(query)
            hideKeyboard()
        }
    }

    private fun handleQueryChange(newText: String?) {
        if (newText.isNullOrEmpty()) {
            popupWindow.dismiss()
            return
        }
        updateSuggestions(newText)
    }

    private fun updateSuggestions(query: String) {
        val helper = DatabaseHelper(applicationContext)
        helper.openDatabase()
        val db = helper.readableDatabase
        wordList.clear()
        val res = db.rawQuery(
            "SELECT word, ipa, type FROM dictionary WHERE word LIKE ? LIMIT 50",
            arrayOf("$query%")
        )
        if (res.moveToFirst()) {
            do {
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                val type = res.getString(res.getColumnIndexOrThrow("type"))
                wordList.add("$word \n/$ipa/ \n$type")
            } while (res.moveToNext())
        }
        res.close()
        if (wordList.isEmpty()) {
            popupWindow.dismiss()
        } else {
            (popupWindow.contentView.findViewById<RecyclerView>(R.id.rvSuggest).adapter as RvAdapter).updateData(
                wordList
            )
            if (!popupWindow.isShowing) {
                popupWindow.showAsDropDown(binding.searchView)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

}