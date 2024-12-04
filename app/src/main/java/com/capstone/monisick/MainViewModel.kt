package com.capstone.monisick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.pref.ResultValue
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.pref.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResultValue<String?>>()
    val loginResult: LiveData<ResultValue<String?>> = _loginResult
    private val _registerResult = MutableLiveData<ResultValue<String?>>()
    val registerResult: LiveData<ResultValue<String?>> = _registerResult
    private val _userSession = MutableLiveData<UserModel?>()
    val userSession: LiveData<UserModel?> = _userSession
    fun login(email: String, password: String) {
        _loginResult.value = ResultValue.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            if (result is ResultValue.Success) {
                val user = UserModel(email = email, token = result.data ?: "", isLogin = true)
                _userSession.value = user
                saveSession(user)
            }
            _loginResult.value = result
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        _registerResult.value = ResultValue.Loading
        viewModelScope.launch {
            val result = repository.register(name, email, password, confirmPassword)
            _registerResult.value = result
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession() {
        viewModelScope.launch {
            _userSession.value = repository.getSession().firstOrNull()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userSession.value = null
        }
    }
}