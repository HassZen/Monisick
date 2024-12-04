package com.capstone.monisick.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.capstone.monisick.database.entity.Medication

@Dao
interface MedicationDao {
    @Insert
    suspend fun insert(medication: Medication)
}