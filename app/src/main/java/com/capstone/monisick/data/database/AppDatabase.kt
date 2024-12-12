package com.capstone.monisick.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.monisick.data.database.dao.LogDao
import com.capstone.monisick.data.database.dao.MakananDao
import com.capstone.monisick.data.database.dao.MedicationDao
import com.capstone.monisick.data.database.dao.ScheduleDao
import com.capstone.monisick.data.database.entity.LogEntity
import com.capstone.monisick.data.database.entity.Makanan
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.data.database.entity.Schedule

@Database(
    entities = [Medication::class, Schedule::class, LogEntity::class, Makanan::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun makananDao(): MakananDao
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