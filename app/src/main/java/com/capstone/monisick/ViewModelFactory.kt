package com.capstone.monisick

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.monisick.data.di.Injection
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.ui.scan.ScanViewModel
import com.capstone.monisick.ui.auth.login.LoginViewModel
import com.capstone.monisick.ui.auth.register.RegisterViewModel
import com.capstone.monisick.ui.history.log.LogViewModel
import com.capstone.monisick.ui.history.makan.MakanViewModel
import com.capstone.monisick.ui.history.obat.ObatViewModel
import com.capstone.monisick.ui.home.HomeViewModel
import com.capstone.monisick.ui.splash.SplashViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel() as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ObatViewModel::class.java) -> {
                ObatViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MakanViewModel::class.java) -> {
                MakanViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LogViewModel::class.java) -> {
                LogViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Kelas ViewModel tidak diketahui : " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}