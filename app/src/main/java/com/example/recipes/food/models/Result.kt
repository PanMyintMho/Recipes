package com.example.recipes.food.models

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Keep
@Parcelize
data class Result(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @SerializedName("glutenFree")
    val glutenFree:Boolean,
    @SerializedName("cheap")
    val cheap: Boolean,
    val dairyFree: Boolean,
    @SerializedName("extendedIngredients")
    val extendedIngredients:@RawValue List<ExtendedIngredient>,
    @SerializedName("id")
    val recipesId: Int,
    @SerializedName("image")
    val image: String?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("servings")
    val servings: Int,
    @SerializedName("sourceName")
    val sourceName: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean,
    ) : Parcelable