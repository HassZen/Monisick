package com.capstone.monisick.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.monisick.data.database.dao.MedicationDao
import com.capstone.monisick.data.database.dao.ScheduleDao
import com.capstone.monisick.data.database.entity.Medication
import com.capstone.monisick.data.database.entity.Schedule

@Database(entities = [Schedule::class, Medication::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
    abstract fun medicationDao(): MedicationDao
}