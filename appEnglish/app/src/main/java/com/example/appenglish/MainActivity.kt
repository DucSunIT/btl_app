package com.example.appenglish

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.PopupWindowCompat
import com.example.appenglish.databinding.ActivityMainBinding
import java.util.zip.Inflater

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var helper: DatabaseHelper
    private lateinit var cursor: Cursor
    private var adt: CustomCursorAdapter? = null
    private val listMember = mutableListOf<ListMember>()
    private val wordList = mutableListOf<SugesstionsWord>()
    private lateinit var popupWindow: PopupWindow
    private lateinit var listView: ListView

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

        // Tìm phần "gạch chân" (SearchPlate) và loại bỏ nền
//        val searchPlate =
//            binding.searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
//        searchPlate?.setBackgroundColor(Color.TRANSPARENT)

        val cursor = null // Hoặc một con trỏ lấy dữ liệu ban đầu nếu có
        adt = CustomCursorAdapter(this, cursor)
        binding.lvSuggest.adapter = adt // Gán adapter cho ListView hoặc RecyclerView
        searchContents()
        addEvents()
    }

    private fun addEvents() {
        handleLayoutVocabulary()
        handleClickProfile()
        displayListUser()
        handleClickVip()
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
                // cnội dung dialog
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

    @SuppressLint("Recycle")
    private fun handleDataWord(helper: DatabaseHelper) {
        val db = helper.readableDatabase
        val res = db.rawQuery("SELECT word, ipa, definition FROM dictionary LIMIT 50", null)
        if (res.moveToFirst()) {
//            wordList.clear()
            do {
                val word = res.getString(res.getColumnIndexOrThrow("word"))
                val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
//                val type = res.getString(res.getColumnIndexOrThrow("type"))
                val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                wordList.add(SugesstionsWord(word, "/$ipa/", definition))
                Log.d("WORD", word)

                Log.d("DEBUG", "DatabaseHelper: $helper")
                Log.d("DEBUG", "wordList: $wordList")
                Log.d("DEBUG", "Database result count: ${res.count}")
            } while (res.moveToNext())
        } else {
            Log.e("Database", "No data found in DICTIONARY")
        }
        res.close()
    }

    private fun searchContents() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val db = helper.readableDatabase
                val query = "${newText}%"

                // Truy vấn cơ sở dữ liệu theo từ khóa người dùng nhập
                cursor = db.rawQuery(
                    "SELECT ID AS _id, WORD, IPA, TYPE FROM DICTIONARY WHERE WORD LIKE ?",
                    arrayOf(query),
                    null
                )

                Log.d("Cursor", "$cursor")
                if (cursor.count > 0) {
                    adt?.changeCursor(cursor)
                    binding.lvSuggest.visibility = View.VISIBLE
                }
                if (newText.isNullOrEmpty()) {
                    binding.lvSuggest.visibility = View.GONE
                }
                return true
            }
        })
    }

}