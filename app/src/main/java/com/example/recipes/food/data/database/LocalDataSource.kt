package com.example.recipes.food.data.database

import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.data.database.entity.FoodJokeEntity
import com.example.recipes.food.data.database.entity.RecipesEntity
import com.example.recipes.food.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun readRecipes(): kotlinx.coroutines.flow.Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }


    fun readFavouriteRecipes(): Flow<List<FavouriteEntity>> {
        return recipesDao.readFavouriteRecipes()

    }
    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDao.readFoodJoke()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }


    suspend fun insertFavouriteRecipes(favouriteEntity: FavouriteEntity){
       recipesDao.insertFavouriteRecipes(favouriteEntity)
    }

    suspend fun deleteFavouriteRecipe(favouriteEntity: FavouriteEntity){
        recipesDao.deleteFavouritRecipes(favouriteEntity)
    }

    suspend fun deleteAllFavouriteRecipes(){
        recipesDao.deleteAllFavouriteRecipes()
    }

}