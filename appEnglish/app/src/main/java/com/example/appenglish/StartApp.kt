package com.example.appenglish

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appenglish.databinding.ActivityMainBinding
import com.example.appenglish.databinding.ActivityStartAppBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityStartAppBinding
class StartApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_app)

        // create view binding
        binding = ActivityStartAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLetGo.setOnClickListener {
            val intentStart = Intent(this, MainActivity::class.java)
            startActivity(intentStart)
        }
    }
}