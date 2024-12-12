package com.capstone.monisick.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "makanan")
@Parcelize
data class Makanan(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idMakanan")
    val id: Int = 0,
    @ColumnInfo(name = "namaMakanan")
    val namaMakanan: String,
    @ColumnInfo(name = "protein")
    val protein: Double,
    @ColumnInfo(name = "carbo")
    val carbo: Double,
    @ColumnInfo(name = "fats")
    val fats: Double,
    @ColumnInfo(name = "mass")
    val mass: Double
) : Parcelable