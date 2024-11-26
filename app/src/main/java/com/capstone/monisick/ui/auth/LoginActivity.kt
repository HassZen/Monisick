package com.capstone.monisick.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.MainActivity
import com.capstone.monisick.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            myToolbar.tvTitleToolbar.text = "Login"

            btnLogin.setOnClickListener {
                // Simpan status login menggunakan SharedPreferences
                val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)  // Tandai pengguna sudah login
                editor.apply()

                // Pindah ke MainActivity setelah login sukses
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Menutup LoginActivity agar tidak bisa kembali ke halaman login
            }

            linearLogin.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            myToolbar.btnBack.visibility = View.GONE
        }
    }
}