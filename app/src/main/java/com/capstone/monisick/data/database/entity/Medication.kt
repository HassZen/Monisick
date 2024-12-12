package com.capstone.monisick.data.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Entity(tableName = "medications")
@Parcelize
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startDate: String,
    val endDate: String,
    val beforeMeal: Boolean,
    val morning: Boolean,
    val afternoon: Boolean,
    val evening: Boolean
) : Parcelable