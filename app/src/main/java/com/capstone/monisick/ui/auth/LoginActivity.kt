package com.capstone.monisick.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.MainActivity
import com.capstone.monisick.MainViewModel
import com.capstone.monisick.data.pref.ResultValue
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.databinding.ActivityLoginBinding
import com.capstone.monisick.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.getSession()
        mainViewModel.userSession.observe(this) { user ->
            if (user?.isLogin == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.apply {
            myToolbar.tvTitleToolbar.text = "Login"
            btnLogin.setOnClickListener {
                val email = edtEmailLogin.text.toString()
                val password = edtPasswordLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    progressBar.visibility = android.view.View.VISIBLE
                    mainViewModel.login(email, password)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            mainViewModel.loginResult.observe(this@LoginActivity) { result ->
                when (result) {
                    is ResultValue.Loading -> {
                        progressBar.visibility = android.view.View.VISIBLE
                    }

                    is ResultValue.Success -> {
                        progressBar.visibility = android.view.View.GONE
                        val token = result.data
                        if (!token.isNullOrEmpty()) {
                            val user = UserModel(
                                email = edtEmailLogin.text.toString(),
                                token = token,
                                isLogin = true
                            )
                            mainViewModel.saveSession(user)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Token tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is ResultValue.Error -> {
                        progressBar.visibility = android.view.View.GONE
                        Toast.makeText(this@LoginActivity, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            tvNoAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}