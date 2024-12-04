package com.capstone.monisick.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.monisick.database.dao.MedicationDao
import com.capstone.monisick.database.dao.ScheduleDao
import com.capstone.monisick.database.entity.Medication
import com.capstone.monisick.database.entity.Schedule

@Database(entities = [Medication::class, Schedule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "monisick_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}