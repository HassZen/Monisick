package com.capstone.monisick.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.MainActivity
import com.capstone.monisick.databinding.ActivityRegisterBinding

class RegisterActivity:AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            myToolbar.tvTitleToolbar.text = "Daftar"

            btnDaftar.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            linearDaftar.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            myToolbar.btnBack.setOnClickListener {
                finish()
            }

        }
    }

}