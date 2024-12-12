package com.capstone.monisick.ui.history.obat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.database.entity.Medication
import kotlinx.coroutines.launch

class ObatViewModel(private val repository: UserRepository) : ViewModel() {
    fun getMeds() = repository.getAllMeds()
    suspend fun saveMeds(medication: Medication) = repository.saveMeds(medication)
    suspend fun deleteMeds(medication: Medication) = repository.deleteMeds(medication.id)
    fun getNotificationSetting(): LiveData<Boolean> {
        return repository.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isNotificationActive: Boolean) {
        viewModelScope.launch {
            repository.saveNotificationSetting(isNotificationActive)
        }
    }
}