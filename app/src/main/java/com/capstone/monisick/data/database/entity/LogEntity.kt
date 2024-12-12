package com.capstone.monisick.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "log")
@Parcelize
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idLog")
    val id: Int = 0,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "tanggal")
    val tanggal: String,
) : Parcelable