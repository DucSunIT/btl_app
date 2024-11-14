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

        val pivActivity = findViewById<ImageView>(R.id.imgPiv)
        pivActivity.setOnClickListener {
            finish()
        }

        val loginGoogle = findViewById<Button>(R.id.btnLoginGoogle)
        loginGoogle.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                setTitle("Thông báo")
                // content
                setMessage("Chức năng đang phát triển !")
                // neagtive -
                setNegativeButton("OK"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            dialog.show()
        }
    }
}