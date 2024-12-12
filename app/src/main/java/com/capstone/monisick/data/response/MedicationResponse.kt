package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class MedicationResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<MedicationData>
)

data class MedicationData(
    @SerializedName("medication_name") val medicationName: String,
    @SerializedName("frequency") val frequency: String,
    @SerializedName("before_after_meal") val beforeAfterMeal: String,
    @SerializedName("schedule_time") val scheduleTime: String,
    @SerializedName("monitoringPeriodId") val monitoringPeriodId: Int
)