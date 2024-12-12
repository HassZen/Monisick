package com.capstone.monisick.data.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun schedulePeriodicNotification(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()
        val repeatInterval = Duration.ofHours(6).plusMinutes(0)
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            repeatInterval.toMillis(),
            TimeUnit.MILLISECONDS,
        )
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "medicationReminderWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    fun cancelNotifications(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("dailyNotificationWork")
    }
}