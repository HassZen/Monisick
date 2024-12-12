package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    val message: String? = null,
    val predictions: Predictions? = null,
    @SerializedName("output_file")
    val outputFile: String? = null
)

data class Predictions(
    val mass: Double? = null,
    val fat: Double? = null,
    val carbohydrates: Double? = null,
    val protein: Double? = null
)