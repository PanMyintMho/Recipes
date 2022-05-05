package com.example.recipes.food.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
@Parcelize
data class FoodJoke(
    @SerializedName("text")
    val text: String
) : Parcelable