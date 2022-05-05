package com.example.recipes.food.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.data.database.entity.FoodJokeEntity
import com.example.recipes.food.data.database.entity.RecipesEntity

@Database(
    entities = [RecipesEntity::class,FavouriteEntity::class,FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
abstract fun recipesDao() : RecipesDao
}