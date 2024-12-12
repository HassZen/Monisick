package com.capstone.monisick.ui.history.makan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.UserRepository
import kotlinx.coroutines.launch

class MakanViewModel(private val repository: UserRepository) : ViewModel() {
    fun getMakanan() = repository.getMakanan()
    fun deleteMakanan(id: Int) {
        viewModelScope.launch {
            repository.deleteMakanan(id)
        }
    }
}