package com.example.recipes.food.data.database

import androidx.room.*
import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.data.database.entity.FoodJokeEntity
import com.example.recipes.food.data.database.entity.RecipesEntity
import com.example.recipes.food.models.FoodJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipes(favouriteEntity: FavouriteEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): kotlinx.coroutines.flow.Flow<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM favourite_recipes_table ORDER BY id ASC")
    fun readFavouriteRecipes(): Flow<List<FavouriteEntity>>

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Delete
    suspend fun deleteFavouritRecipes(favouriteEntity: FavouriteEntity)

    @Query("DELETE FROM favourite_recipes_table")
    suspend fun deleteAllFavouriteRecipes()

}