package com.capstone.monisick.data.pref

import com.capstone.monisick.data.response.ResponseInvalid
import com.capstone.monisick.data.response.ResponseLogin
import com.capstone.monisick.data.retrofit.ApiService
import com.capstone.monisick.data.retrofit.LoginRequest
import com.capstone.monisick.data.retrofit.RegisterRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val preference: UserPreference
) {
    suspend fun register(
        name: String,
        email: String,
        password: String,
        confPassword: String
    ): ResultValue<String?> {
        return try {
            val registerRequest = RegisterRequest(name, email, password, confPassword)
            val successResponse = apiService.register(registerRequest)
            val message = successResponse.message
            ResultValue.Success(message)
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                400 -> "Email sudah dimasukkan"
                else -> {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ResponseInvalid::class.java)
                    errorBody.message.toString()
                }
            }
            ResultValue.Error(errorMessage)
        }
    }

    suspend fun login(email: String, password: String): ResultValue<String?> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val successResponse = apiService.login(loginRequest)
            if (successResponse.error == true) {
                ResultValue.Error(successResponse.message ?: "Kesalahan tidak diketahui")
            } else {
                val token = successResponse.loginResult?.token
                ResultValue.Success(token)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseLogin::class.java)
            ResultValue.Error(errorResponse.message ?: "Terjadi kesalahan")
        } catch (e: Exception) {
            ResultValue.Error("Terjadi kesalahan tak terduga")
        }
    }

    fun getSession(): Flow<UserModel> {
        return preference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        preference.saveSession(user)
    }

    suspend fun logout() {
        preference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, pref)
            }.also { instance = it }
    }
}