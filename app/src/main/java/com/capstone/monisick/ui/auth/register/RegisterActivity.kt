package com.capstone.monisick.ui.auth.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.ViewModelFactory
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.databinding.ActivityRegisterBinding
import com.capstone.monisick.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val mainViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            myToolbar.tvTitleToolbar.text = "Daftar"
            btnDaftar.setOnClickListener {
                val name = edtUsername.text.toString()
                val email = edtEmailDaftar.text.toString()
                val password = edtPasswordDaftar.text.toString()
                val confirmPassword = edtPasswordConfirm.text.toString()
                if (validateInputs(name, email, password, confirmPassword)) {
                    mainViewModel.register(name, email, password, confirmPassword)
                        .observe(this@RegisterActivity) { result ->
                            when (result) {
                                is ResultValue.Loading -> {
                                }

                                is ResultValue.Success -> {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Pendaftaran berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navigateToLogin()
                                }

                                is ResultValue.Error -> {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        result.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                }
            }
            tvAlreadyAccount.setOnClickListener {
                navigateToLogin()
            }
        }
        binding.myToolbar.btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
                false
            }

            password != confirmPassword -> {
                Toast.makeText(
                    this,
                    "Kata Sandi dan Konfirmasi Kata Sandi tidak cocok",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}