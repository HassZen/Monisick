package com.capstone.monisick.ui.history.log

import androidx.lifecycle.ViewModel
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.database.entity.LogEntity

class LogViewModel(private val repository: UserRepository) : ViewModel() {
    fun getLogs() = repository.getDailyLog()
    suspend fun saveLog(log: LogEntity) = repository.saveLog(log)
    suspend fun deleteLog(log: LogEntity) = repository.deleteLog(log.id)
    suspend fun updateLog(log: LogEntity) = repository.updateLog(log)
}