package com.example.appenglish

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // gọi hàm
        handleClickPreviousLogin()
        handleClickBtnLogin()
    }

    /*
    * Hàm có chức năng xử lý sự kiện khi người dụng click vào
    * nút đăng nhập bằng tài khoản google
    * sẽ hiện ra một thông báo (AlertDialog)*/
    private fun handleClickBtnLogin() {
        val loginGoogle = findViewById<Button>(R.id.btnLoginGoogle)
        loginGoogle.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                // tiêu đề của dialog
                setTitle("Thông báo")
                // Nội dung của dialog
                setMessage("Chức năng đang phát triển !")
                // Nếu người dùng bấm OK dialog sẽ ẩn đi
                setNegativeButton("OK"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            dialog.show()
        }
    }

    /*Hàm có chức năng xử lý khi người dùng
    * bấm vào mũi tên quay lại ở LoginActivity
    * sẽ quay lại activity trước*/
    private fun handleClickPreviousLogin() {
        val pivActivity = findViewById<ImageView>(R.id.imgPiv)
        pivActivity.setOnClickListener {
            finish()
        }
    }
}