package com.capstone.monisick.data.di

import android.content.Context
import com.capstone.monisick.data.pref.UserPreference
import com.capstone.monisick.data.pref.UserRepository
import com.capstone.monisick.data.pref.dataStore
import com.capstone.monisick.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}