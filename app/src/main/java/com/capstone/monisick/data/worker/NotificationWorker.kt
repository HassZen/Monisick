package com.capstone.monisick.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.capstone.monisick.R
import com.capstone.monisick.data.UserRepository
import com.capstone.monisick.data.database.AppDatabase
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.data.pref.UserPreference
import com.capstone.monisick.data.pref.dataStore
import com.capstone.monisick.data.retrofit.ApiConfig
import com.capstone.monisick.data.retrofit.ml.MLApiConfig

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    companion object {
        private val TAG = NotificationWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "Monisick"
    }

    override fun doWork(): Result {
        val repository = getRepository(applicationContext)
        val latestMedication = repository.getLatestMedication()
        return if (latestMedication != null) {
            showNotification(latestMedication)
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun getRepository(context: Context): UserRepository {
        val database = AppDatabase.getDatabase(context)
        return UserRepository.getInstance(
            apiService = ApiConfig.getApiService(""),
            mlService = MLApiConfig.provideApiService(),
            pref = UserPreference.getInstance(context.dataStore),
            logDao = database.logDao(),
            makananDao = database.makananDao(),
            medicationDao = database.medicationDao()
        )
    }

    private fun showNotification(medication: Medication) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for medication reminders"
            }
            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Determine the 'beforeMeal' text based on the medication's 'beforeMeal' flag
        val beforeMealText = if (medication.beforeMeal) "sebelum makan" else "sesudah makan"

        val notificationText = "Jangan lupa minum obat ${medication.name} $beforeMealText ya!"

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm)
            .setContentTitle("Waktunya minum obat!")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}