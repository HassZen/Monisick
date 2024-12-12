package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class DailyLogResponse(
    @SerializedName("log_date") val logDate: String,
    @SerializedName("content") val content: String
)