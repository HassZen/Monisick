package com.capstone.monisick.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.response.ResponseRegister
import com.capstone.monisick.data.utils.ResultValue

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): LiveData<ResultValue<ResponseRegister>> =
        repository.registers(name, email, pass, confirmPass)
}