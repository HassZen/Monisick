package com.capstone.monisick.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.capstone.monisick.data.database.entity.LogEntity

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logEntity: LogEntity)

    @Query("SELECT * FROM log")
    fun getAllLog(): LiveData<List<LogEntity>>

    @Query("DELETE FROM log WHERE idLog = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(logEntity: LogEntity)
}