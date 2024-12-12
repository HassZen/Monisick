package com.capstone.monisick.ui.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.MainActivity
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.databinding.ActivityLoginBinding
import com.capstone.monisick.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mainViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            myToolbar.tvTitleToolbar.text = "Login"
            btnLogin.setOnClickListener {
                val email = edtEmailLogin.text.toString()
                val password = edtPasswordLogin.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    mainViewModel.login(email, password).observe(this@LoginActivity) { result ->
                        when (result) {
                            is ResultValue.Loading -> {
                            }

                            is ResultValue.Success -> {
                                result.data.loginResult?.token?.let { it1 ->
                                    authComplete(
                                        it1
                                    )
                                }
                            }

                            is ResultValue.Error -> {
                                Toast.makeText(
                                    this@LoginActivity,
                                    result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Silahkan isi semua kolom",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            tvNoAccount.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        binding.myToolbar.btnBack.setOnClickListener {
            finishAffinity()
        }
    }

    private fun authComplete(token: String) {
        val email = binding.edtEmailLogin.text.toString()
        mainViewModel.saveSession(UserModel(email, token))
        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}