package com.example.recipes.food.data


import com.example.recipes.food.data.network.FoodRecipesApi
import com.example.recipes.food.models.FoodJoke
import com.example.recipes.food.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipe(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getGoodFoodJoke(apiKey: String): Response<FoodJoke> {

        return foodRecipesApi.getFoodJoke(apiKey)
    }
}