package com.capstone.monisick.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.response.ResponseLogin
import kotlinx.coroutines.launch
import com.capstone.monisick.data.utils.ResultValue

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, pass: String): LiveData<ResultValue<ResponseLogin>> =
        repository.logins(email, pass)
}