package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class MonitoringResponse(
    @SerializedName("monitoring_name") val monitoringName: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("status") val status: String
)
