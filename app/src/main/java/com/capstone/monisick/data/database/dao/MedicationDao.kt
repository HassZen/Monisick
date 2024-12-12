package com.capstone.monisick.data.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.capstone.monisick.data.database.entity.Medication

@Dao
interface MedicationDao {

    @Insert
    suspend fun insert(medication: Medication)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeds(medication: Medication)

    @Query("SELECT * FROM medications")
    fun getAllMeds(): LiveData<List<Medication>>

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(medication: Medication)

    @Query("SELECT * FROM medications ORDER BY id DESC LIMIT 1")
    fun getLatestMedication(): Medication?
}