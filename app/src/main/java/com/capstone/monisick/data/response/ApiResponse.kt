package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("msg") val message: String
)