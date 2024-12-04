package com.capstone.monisick.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.monisick.MainViewModel
import com.capstone.monisick.data.pref.ResultValue
import com.capstone.monisick.databinding.ActivityRegisterBinding
import com.capstone.monisick.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val mainViewModel: MainViewModel by viewModels {
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

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password == confirmPassword) {
                        // Show loading indicator
                        progressBar.visibility = android.view.View.VISIBLE
                        mainViewModel.register(name, email, password, confirmPassword)
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Password and Confirm Password do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            mainViewModel.registerResult.observe(this@RegisterActivity) { result ->
                when (result) {
                    is ResultValue.Loading -> {
                        // Show loading if needed
                        progressBar.visibility = android.view.View.VISIBLE
                    }

                    is ResultValue.Success -> {
                        // Hide loading indicator after success
                        progressBar.visibility = android.view.View.GONE
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is ResultValue.Error -> {
                        // Hide loading indicator after error
                        progressBar.visibility = android.view.View.GONE
                        Toast.makeText(this@RegisterActivity, result.error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            tvAlreadyAccount.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}