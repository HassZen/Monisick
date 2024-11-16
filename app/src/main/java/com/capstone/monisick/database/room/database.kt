package com.capstone.monisick.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.monisick.database.dao.MedicationDao
import com.capstone.monisick.database.dao.ScheduleDao
import com.capstone.monisick.database.entity.Medication
import com.capstone.monisick.database.entity.Schedule

@Database(entities = [Schedule::class, Medication::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun medicationDao(): MedicationDao
}