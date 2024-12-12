package com.capstone.monisick.data.retrofit.ml

import com.capstone.monisick.data.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MLApiService {
    @Multipart
    @POST("predict/")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): PredictResponse
}