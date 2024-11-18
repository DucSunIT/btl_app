package com.example.appenglish

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appenglish.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var helper: DatabaseHelper
    private lateinit var cursor: CursorAdapter
    private val listMember = mutableListOf<ListMember>()
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
        handleLayoutSearch()
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
        handleDatabase(helper)
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

    /*Hàm có chức năng xử lý sự kiện khi người dùng tìm từ*/
    private fun handleLayoutSearch() {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            /*Nếu người dùng sau khi nhập xong từ bấm nút xong trên
            * bàn phím sẽ nhận sự kiện tìm kiếm*/
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                /*biến link đến view edtSearch*/
                val searchQuery = binding.edtSearch.text.toString().trim()
                /*Kiếm tra nếu trong edtSearch rỗng*/
                if (searchQuery.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show()
                } else {
                    /*Gọi đến Activity sau khi tra từ*/
                    val intentSearch = Intent(this, SearchWordActivity::class.java)
                    startActivity(intentSearch)
                }
                true
            } else {
                false
            }
        }
    }

    /*Hàm có chức năng truy vấn csdl và đẩy dữ liệu vào List database*/
    @SuppressLint("Recycle")
    private fun handleDatabase(helper: DatabaseHelper) {
        val db = helper.readableDatabase
        val res = db.rawQuery("SELECT FULLNAME FROM MEMBERS", null)
        if(res.moveToFirst()){
            listMember.clear()
            do{
                val fullName = res.getString(0)
                listMember.add(ListMember(R.drawable.profile, fullName, R.drawable.star_vip))
            }while (res.moveToNext())
        }
        res.close()
    }
}