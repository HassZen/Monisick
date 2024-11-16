package com.capstone.monisick.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.capstone.monisick.database.entity.Medication

@Dao
interface MedicationDao {
    @Insert
    suspend fun insert(medication: Medication)

    @Query("SELECT * FROM medications")
    suspend fun getAllMedications(): List<Medication>
}