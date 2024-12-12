package com.capstone.monisick.data.response

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("food_time") val foodTime: String,
    @SerializedName("calories") val calories: Int,
    @SerializedName("proteins") val proteins: Int,
    @SerializedName("carbo") val carbo: Int,
    @SerializedName("fats") val fats: Int,
    @SerializedName("massa") val massa: Int,
    @SerializedName("food_date") val foodDate: String
)