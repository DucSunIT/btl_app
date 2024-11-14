package com.example.appenglish

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
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

    private fun handleClickVip() {
        binding.imgPremium.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Thông báo")
                // content
                setMessage("Chức năng đang phát triển !")

                // neagtive -
                setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun displayListUser() {
        val listUser = mutableListOf<ListMember>()
        val listNameMember = listOf(
            "Phạm Tiến Đức",
            "Nông Văn Nguyên",
            "Lý Văn Phòng",
            "Đỗ Mạnh Hùng",
            "Quách Trọng Long"
        )

        for (pos in listNameMember.indices) {
            listUser.add(ListMember(R.drawable.profile, listNameMember[pos], R.drawable.star_vip))
        }
        val customLV = CustomListMember(this, listUser)
        binding.lvMemberGroup.adapter = customLV
    }

    private fun handleClickProfile() {
        binding.imgProfile.setOnClickListener {
            val intentProfile = Intent(this, LoginActivity::class.java)
            startActivity(intentProfile)
        }
    }

    private fun handleLayoutVocabulary() {
        binding.layoutDictonary.setOnClickListener {
            val intentMainVocabulary = Intent(this, MainVocabulary::class.java)
            startActivity(intentMainVocabulary)
        }
    }


    private fun handleLayoutSearch() {
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = binding.edtSearch.text.toString().trim()
                if (searchQuery.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show()
                } else {
                    val intentSearch = Intent(this, SearchWordEnglish::class.java)
                    startActivity(intentSearch)
                }
                true
            } else {
                false
            }
        }


    }
}