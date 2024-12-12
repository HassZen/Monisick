package com.capstone.monisick.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.monisick.data.database.dao.LogDao
import com.capstone.monisick.data.database.dao.MakananDao
import com.capstone.monisick.data.database.dao.MedicationDao
import com.capstone.monisick.data.database.entity.LogEntity
import com.capstone.monisick.data.database.entity.Makanan
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.data.utils.ResultValue
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.pref.UserPreference
import com.capstone.monisick.data.response.ApiResponse
import com.capstone.monisick.data.response.DailyLogResponse
import com.capstone.monisick.data.response.FoodResponse
import com.capstone.monisick.data.response.MedicationResponse
import com.capstone.monisick.data.response.MonitoringResponse
import com.capstone.monisick.data.response.PredictResponse
import com.capstone.monisick.data.response.ResponseInvalid
import com.capstone.monisick.data.response.ResponseLogin
import com.capstone.monisick.data.response.ResponseRegister
import com.capstone.monisick.data.response.SavePredictResponse
import com.capstone.monisick.data.retrofit.ApiConfig
import com.capstone.monisick.data.retrofit.ApiService
import com.capstone.monisick.data.retrofit.DailyLogRequest
import com.capstone.monisick.data.retrofit.DailyLogUpdateRequest
import com.capstone.monisick.data.retrofit.FoodRequest
import com.capstone.monisick.data.retrofit.LoginRequest
import com.capstone.monisick.data.retrofit.MedicationRequest
import com.capstone.monisick.data.retrofit.MedicationUpdateRequest
import com.capstone.monisick.data.retrofit.MonitoringRequest
import com.capstone.monisick.data.retrofit.MonitoringUpdateRequest
import com.capstone.monisick.data.retrofit.RegisterRequest
import com.capstone.monisick.data.retrofit.SavePredictRequest
import com.capstone.monisick.data.retrofit.ml.MLApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val mlService: MLApiService,
    private val preference: UserPreference,
    private val logDao: LogDao,
    private val makananDao: MakananDao,
    private val medicationDao: MedicationDao
) {
    fun registers(
        name: String,
        email: String,
        pass: String,
        confPass: String
    ): LiveData<ResultValue<ResponseRegister>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = RegisterRequest(name, email, pass, confPass)
            val response = apiService.register(request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            Log.d("daftar", e.message.toString())
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ResponseInvalid::class.java)
            val errorMessage = errorBody.message
            emit(ResultValue.Error(errorMessage.toString()))
        }
    }

    fun logins(
        email: String,
        pass: String
    ): LiveData<ResultValue<ResponseLogin>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = LoginRequest(email, pass)
            val response = apiService.login(request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            Log.d("login", e.message.toString())
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ResponseInvalid::class.java)
            val errorMessage = errorBody.message
            emit(ResultValue.Error(errorMessage.toString()))
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
        instance = null
    }

    fun addMonitoring(
        monitoringName: String,
        startDate: String,
        endDate: String,
        status: String
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = MonitoringRequest(monitoringName, startDate, endDate, status)
            val response = apiService.addMonitoring(request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun getAllMonitoring(): LiveData<ResultValue<List<MonitoringResponse>>> = liveData {
        emit(ResultValue.Loading)

        try {
            // Get the token from UserPreference
            val token = preference.getSession().first().token

            if (token.isNotEmpty()) {
                // Reinitialize ApiService with the token
                val apiService = ApiConfig.getApiService(token)

                // Make the API request
                val response = apiService.getAllMonitoring()
                emit(ResultValue.Success(response))
            } else {
                emit(ResultValue.Error("Authorization token is missing"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            emit(ResultValue.Error(errorBody ?: "Unknown error occurred"))
        } catch (e: Exception) {
            emit(ResultValue.Error("An unexpected error occurred: ${e.message}"))
        }
    }

    fun updateMonitoring(
        id: String,
        monitoringName: String,
        startDate: String,
        endDate: String
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = MonitoringUpdateRequest(monitoringName, startDate, endDate)
            val response = apiService.updateMonitoring(id, request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun deleteMonitoring(id: String): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.deleteMonitoring(id)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    // Medication Functions
    fun addMedication(
        monitoringPeriodId: String,
        medicationName: String,
        frequency: List<String>,
        beforeAfterMeal: String,
        scheduleTime: List<String>
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request =
                MedicationRequest(medicationName, frequency, beforeAfterMeal, scheduleTime)
            val response = apiService.addMedication(monitoringPeriodId, request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun getAllMedications(monitoringPeriodId: String): LiveData<ResultValue<MedicationResponse>> =
        liveData {
            emit(ResultValue.Loading)
            try {
                val response = apiService.getAllMedications(monitoringPeriodId)
                emit(ResultValue.Success(response))
            } catch (e: HttpException) {
                val errorMessage = handleHttpException(e)
                emit(ResultValue.Error(errorMessage))
            }
        }

    fun updateMedication(
        monitoringPeriodId: String,
        medicationId: String,
        medicationName: String,
        frequency: List<String>,
        beforeAfterMeal: String,
        scheduleTime: List<String>
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request =
                MedicationUpdateRequest(medicationName, frequency, beforeAfterMeal, scheduleTime)
            val response = apiService.updateMedication(monitoringPeriodId, medicationId, request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun deleteMedication(
        monitoringPeriodId: String,
        medicationId: String
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.deleteMedication(monitoringPeriodId, medicationId)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun getLatestMedication(): Medication? {
        return medicationDao.getLatestMedication()
    }

    fun getNotificationSetting(): Flow<Boolean> {
        return preference.getNotificationSetting()
    }
    suspend fun saveNotificationSetting(isNotificationActive: Boolean) {
        preference.saveNotificationSetting(isNotificationActive)
    }

    // Food Functions
    fun addFood(
        foodTime: String,
        calories: Int,
        proteins: Int,
        carbo: Int,
        fats: Int,
        massa: Int
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = FoodRequest(foodTime, calories, proteins, carbo, fats, massa)
            val response = apiService.addFood(request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun getAllFoods(monitoringPeriodId: String): LiveData<ResultValue<List<FoodResponse>>> =
        liveData {
            emit(ResultValue.Loading)
            try {
                val response = apiService.getAllFoods(monitoringPeriodId)
                emit(ResultValue.Success(response))
            } catch (e: HttpException) {
                val errorMessage = handleHttpException(e)
                emit(ResultValue.Error(errorMessage))
            }
        }

    fun deleteFood(id: String): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.deleteFood(id)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    fun addDailyLog(
        monitoringPeriodId: String,
        request: DailyLogRequest
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.addDailyLog(monitoringPeriodId, request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    // Get all daily logs
    fun getAllDailyLogs(monitoringPeriodId: String): LiveData<ResultValue<List<DailyLogResponse>>> =
        liveData {
            emit(ResultValue.Loading)
            try {
                val response = apiService.getAllLogs(monitoringPeriodId)
                emit(ResultValue.Success(response))
            } catch (e: HttpException) {
                val errorMessage = handleHttpException(e)
                emit(ResultValue.Error(errorMessage))
            }
        }

    // Update a daily log
    fun updateDailyLog(
        logId: String,
        request: DailyLogUpdateRequest
    ): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.updateDailyLog(logId, request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    // Delete a daily log
    fun deleteDailyLog(logId: String): LiveData<ResultValue<ApiResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val response = apiService.deleteDailyLog(logId)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    //local database
    fun getAllMeds(): LiveData<List<Medication>> {
        return medicationDao.getAllMeds()
    }

    suspend fun saveMeds(medication: Medication) {
        medicationDao.insertMeds(medication)
    }

    suspend fun deleteMeds(id: Int) {
        medicationDao.delete(id)
    }

    suspend fun updateMeds(medication: Medication) {
        medicationDao.update(medication)
    }

    //log
    fun getDailyLog(): LiveData<List<LogEntity>> {
        return logDao.getAllLog()
    }

    suspend fun saveLog(log: LogEntity) {
        logDao.insert(log)
    }

    suspend fun deleteLog(id: Int) {
        logDao.delete(id)
    }

    suspend fun updateLog(log: LogEntity) {
        logDao.update(log)
    }

    //makanan
    fun getMakanan(): LiveData<List<Makanan>> {
        return makananDao.getAllMakanan()
    }

    suspend fun saveMakanan(makanan: Makanan) {
        makananDao.insert(makanan)
    }

    suspend fun deleteMakanan(id: Int) {
        makananDao.delete(id)
    }

    suspend fun updateMakanan(makanan: Makanan) {
        makananDao.update(makanan)
    }

    fun getPrediction(
        imageFile: File,
    ): LiveData<ResultValue<PredictResponse>> = liveData {
        emit(ResultValue.Loading)
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse =
                mlService.uploadFile(multipartBody)
            emit(ResultValue.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
            emit(ResultValue.Error(errorResponse.message))
        }
    }

    fun savePredict(
        foodName: String,
        mass: Double,
        fat: Double,
        carbohydrates: Double,
        protein: Double
    ): LiveData<ResultValue<SavePredictResponse>> = liveData {
        emit(ResultValue.Loading)
        try {
            val request = SavePredictRequest(foodName, mass, fat, carbohydrates, protein)
            val response = apiService.savePredict(request)
            emit(ResultValue.Success(response))
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e)
            emit(ResultValue.Error(errorMessage))
        }
    }

    private fun handleHttpException(e: HttpException): String {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ResponseInvalid::class.java)
        return errorBody.message ?: "Terjadi kesalahan yang tidak terduga."
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            mlService: MLApiService,
            pref: UserPreference,
            logDao: LogDao,
            makananDao: MakananDao,
            medicationDao: MedicationDao
        ) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(
                    apiService,
                    mlService,
                    pref,
                    logDao,
                    makananDao,
                    medicationDao
                )
            }.also { instance = it }
    }
}