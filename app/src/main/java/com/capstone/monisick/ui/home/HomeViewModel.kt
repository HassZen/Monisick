package com.capstone.monisick.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.response.ApiResponse
import com.capstone.monisick.data.response.MonitoringResponse
import com.capstone.monisick.data.utils.ResultValue
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()

    fun addMonitoring(
        monitoringName: String,
        startDate: String,
        endDate: String,
        status: String
    ): LiveData<ResultValue<ApiResponse>> =
        repository.addMonitoring(monitoringName, startDate, endDate, status)

    fun getAllMonitoring(): LiveData<ResultValue<List<MonitoringResponse>>> =
        repository.getAllMonitoring()

    private val _userSession = MutableLiveData<UserModel?>()
    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _userSession.value = null
        }
    }

    // New function to refresh the monitoring data
    fun refreshMonitoringData() {
        // Trigger loading state or call a function to re-fetch the data
        getAllMonitoring()
    }
}
