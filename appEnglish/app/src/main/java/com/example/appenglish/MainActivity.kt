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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appenglish.databinding.ActivityMainBinding
import com.example.appenglish.databinding.PopupSuggestBinding
import java.util.zip.Inflater

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
        popupSuggest()
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


    private fun popupSuggest() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_suggest, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            800
        )
        popupWindow.isFocusable = false
        popupWindow.isOutsideTouchable = true

        // Gắn RecyclerView vào PopupWindow
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rvSuggest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(wordList) { selectedItem ->
            binding.searchView.setQuery(selectedItem, false)
            popupWindow.dismiss()
        }

        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.custom_line)!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        // Hiển thị PopupWindow khi SearchView được focus
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                popupWindow.showAsDropDown(binding.searchView)
            } else {
                popupWindow.dismiss()
            }
        }

        // Lọc gợi ý khi người dùng nhập
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                popupWindow.dismiss()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    popupWindow.dismiss()
                    return false
                }
                val query = "${newText}%"
                helper = DatabaseHelper(applicationContext)
                helper.openDatabase()
                val db = helper.readableDatabase
                wordList.clear()
                val res = db.rawQuery(
                    "select word, ipa, type from dictionary where word like ?", arrayOf(query), null
                )
                Log.d("QUERY", "$query")
                if (res.moveToFirst()) {
                    do {
                        val word = res.getString(res.getColumnIndexOrThrow("word"))
                        val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                        val type = res.getString(res.getColumnIndexOrThrow("type"))
                        Log.d("DATA", "Fetched word: $word")
                        wordList.add("$word \n/$ipa/ \n$type")
                    } while (res.moveToNext())
                }
                res.close()

                if (wordList.isEmpty()) {
                    popupWindow.dismiss()
                } else {
                    adapter.updateData(wordList)
                    if (!popupWindow.isShowing) {
                        popupWindow.showAsDropDown(binding.searchView)
                    }
                }
                return true
            }
        })
    }


}