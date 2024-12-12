package com.capstone.monisick.data.retrofit.ml

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MLApiConfig {
    companion object {
        private fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }

        fun provideApiService(): MLApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://monisick-app-ml-1035188713587.asia-southeast2.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient())
                .build()
            return retrofit.create(MLApiService::class.java)
        }
    }
}