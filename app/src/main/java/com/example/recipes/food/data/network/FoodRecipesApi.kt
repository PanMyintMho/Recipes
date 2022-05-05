package com.example.recipes.food.data.network

import com.example.recipes.food.models.FoodJoke
import com.example.recipes.food.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipe(
        @QueryMap query: Map<String, String>
    ): Response<FoodRecipe>


    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipe>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey:String
    ):Response<FoodJoke>

}