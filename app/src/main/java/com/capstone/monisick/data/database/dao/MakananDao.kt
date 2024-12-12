package com.capstone.monisick.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.capstone.monisick.data.database.entity.Makanan

@Dao
interface MakananDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(makanan: Makanan)

    @Query("SELECT * FROM makanan")
    fun getAllMakanan(): LiveData<List<Makanan>>

    @Query("DELETE FROM makanan WHERE idMakanan = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(makanan: Makanan)
}