package com.example.recipes.food.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.food.models.Result
import com.example.recipes.food.util.Constants.Companion.FAVOURITE_RECIPES_TABLE


@Entity(tableName = FAVOURITE_RECIPES_TABLE)
class FavouriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)