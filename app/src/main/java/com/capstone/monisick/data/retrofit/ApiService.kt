package com.capstone.monisick.data.retrofit

import com.capstone.monisick.data.response.ApiResponse
import com.capstone.monisick.data.response.DailyLogResponse
import com.capstone.monisick.data.response.FoodResponse

import com.capstone.monisick.data.response.MedicationResponse
import com.capstone.monisick.data.response.MonitoringResponse
import com.capstone.monisick.data.response.ResponseLogin
import com.capstone.monisick.data.response.ResponseRegister
import com.capstone.monisick.data.response.SavePredictResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface ApiService {
    // Existing methods
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ResponseRegister

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ResponseLogin

    // Monitoring Periods
    @POST("monitoring-periods")
    suspend fun addMonitoring(@Body request: MonitoringRequest): ApiResponse

    @GET("monitoring-periods")
    suspend fun getAllMonitoring(): List<MonitoringResponse>

    @PUT("monitoring-periods/{id}")
    suspend fun updateMonitoring(
        @Path("id") id: String,
        @Body request: MonitoringUpdateRequest
    ): ApiResponse

    @DELETE("monitoring-periods/{id}")
    suspend fun deleteMonitoring(@Path("id") id: String): ApiResponse

    // Medications
    @POST("monitoring-periods/{monitoringPeriodId}/medications")
    suspend fun addMedication(
        @Path("monitoringPeriodId") monitoringPeriodId: String,
        @Body request: MedicationRequest
    ): ApiResponse

    @GET("monitoring-periods/{monitoringPeriodId}/medications")
    suspend fun getAllMedications(
        @Path("monitoringPeriodId") monitoringPeriodId: String
    ): MedicationResponse

    @PUT("monitoring-periods/{monitoringPeriodId}/medications/{id}")
    suspend fun updateMedication(
        @Path("monitoringPeriodId") monitoringPeriodId: String,
        @Path("id") medicationId: String,
        @Body request: MedicationUpdateRequest
    ): ApiResponse

    @DELETE("monitoring-periods/{monitoringPeriodId}/medications/{id}")
    suspend fun deleteMedication(
        @Path("monitoringPeriodId") monitoringPeriodId: String,
        @Path("id") medicationId: String
    ): ApiResponse

    // Foods
    @POST("food")
    suspend fun addFood(@Body request: FoodRequest): ApiResponse

    @GET("monitoring-periods/{monitoringPeriodId}/foods")
    suspend fun getAllFoods(
        @Path("monitoringPeriodId") monitoringPeriodId: String
    ): List<FoodResponse>

    @DELETE("food/{id}")
    suspend fun deleteFood(@Path("id") id: String): ApiResponse

    // Daily Logs
    @POST("monitoring-periods/{monitoringPeriodId}/logs")
    suspend fun addDailyLog(
        @Path("monitoringPeriodId") monitoringPeriodId: String,
        @Body request: DailyLogRequest
    ): ApiResponse

    @GET("monitoring-periods/{monitoringPeriodId}/logs")
    suspend fun getAllLogs(
        @Path("monitoringPeriodId") monitoringPeriodId: String
    ): List<DailyLogResponse>

    @PUT("logs/{logId}")
    suspend fun updateDailyLog(
        @Path("logId") logId: String,
        @Body request: DailyLogUpdateRequest
    ): ApiResponse

    @DELETE("logs/{logId}")
    suspend fun deleteDailyLog(@Path("logId") logId: String): ApiResponse

    @POST("/savepredict")
    suspend fun savePredict(
        @Body request: SavePredictRequest
    ): SavePredictResponse
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

data class MonitoringRequest(
    @SerializedName("monitoring_name") val monitoringName: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("status") val status: String
)

data class MonitoringUpdateRequest(
    @SerializedName("monitoring_name") val monitoringName: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
)

data class MedicationRequest(
    @SerializedName("medication_name") val medicationName: String,
    @SerializedName("frequency") val frequency: List<String>,
    @SerializedName("before_after_meal") val beforeAfterMeal: String,
    @SerializedName("schedule_time") val scheduleTime: List<String>
)

data class MedicationUpdateRequest(
    @SerializedName("medication_name") val medicationName: String,
    @SerializedName("frequency") val frequency: List<String>,
    @SerializedName("before_after_meal") val beforeAfterMeal: String,
    @SerializedName("schedule_time") val scheduleTime: List<String>
)

data class FoodRequest(
    @SerializedName("food_time") val foodTime: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("proteins") val proteins: Int,
    @SerializedName("carbo") val carbo: Int,
    @SerializedName("fats") val fats: Int,
    @SerializedName("massa") val massa: Int
)

data class DailyLogRequest(
    @SerializedName("content") val content: String
)

data class DailyLogUpdateRequest(
    @SerializedName("content") val content: String
)

data class SavePredictRequest(
    @SerializedName("foodName")
    val foodName: String,
    @SerializedName("mass")
    val mass: Double,
    @SerializedName("fat")
    val fat: Double,
    @SerializedName("carbohydrates")
    val carbohydrates: Double,
    @SerializedName("protein")
    val protein: Double
)