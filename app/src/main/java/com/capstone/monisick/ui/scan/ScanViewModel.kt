package com.capstone.monisick.ui.scan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.database.entity.Makanan
import com.capstone.monisick.data.pref.UserModel
import com.capstone.monisick.data.response.PredictResponse
import kotlinx.coroutines.launch
import java.io.File
import com.capstone.monisick.data.utils.ResultValue
class ScanViewModel(private val repository: UserRepository) : ViewModel() {
    private val _uploadPredictionResult = MutableLiveData<ResultValue<PredictResponse>>()
    val uploadPredictionResult: LiveData<ResultValue<PredictResponse>>
        get() = _uploadPredictionResult
    fun uploadPrediction(file: File) = viewModelScope.launch {
        _uploadPredictionResult.value = ResultValue.Loading
        try {
            repository.getPrediction(file).observeForever { result ->
                _uploadPredictionResult.value = result
            }
        } catch (e: Exception) {
            _uploadPredictionResult.value = ResultValue.Error(e.localizedMessage ?: "Terjadi kesalahan saat mengunggah Makanan")
        }
    }
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    suspend fun saveMakanan(makanan: Makanan) = repository.saveMakanan(makanan)
}