package com.capstone.monisick.data.di

import android.content.Context
import com.capstone.monisick.data.pref.UserPreference
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.database.AppDatabase
import com.capstone.monisick.data.pref.dataStore
import com.capstone.monisick.data.retrofit.ApiConfig
import com.capstone.monisick.data.retrofit.ml.MLApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val mlService = MLApiConfig.provideApiService()
        val logDao = AppDatabase.getDatabase(context).logDao()
        val makananDao = AppDatabase.getDatabase(context).makananDao()
        val medDao = AppDatabase.getDatabase(context).medicationDao()
        return UserRepository.getInstance(apiService, mlService, pref, logDao, makananDao, medDao)
    }
}