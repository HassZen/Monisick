package com.capstone.monisick.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.capstone.monisick.data.database.entity.Schedule

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insert(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)
}