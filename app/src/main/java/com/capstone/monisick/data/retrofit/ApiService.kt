package com.capstone.monisick.data.retrofit

import com.capstone.monisick.data.response.ResponseLogin
import com.capstone.monisick.data.response.ResponseRegister
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): ResponseRegister

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ResponseLogin
}

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("confPassword") val confPassword: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)