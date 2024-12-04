package com.capstone.monisick.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int?,
    val beforeMeal: Boolean,
    val morning: Boolean,
    val afternoon: Boolean,
    val evening: Boolean
)