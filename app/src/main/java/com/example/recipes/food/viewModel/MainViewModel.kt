package com.example.recipes.food.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.recipes.food.data.Repository
import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.data.database.entity.FoodJokeEntity
import com.example.recipes.food.data.database.entity.RecipesEntity
import com.example.recipes.food.models.FoodJoke
import com.example.recipes.food.models.FoodRecipe
import com.example.recipes.food.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository, application: Application
) : AndroidViewModel(application) {

    /**Room DATABASE */

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavourideRecipes: LiveData<List<FavouriteEntity>> =
        repository.local.readFavouriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }


    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }

    fun insertFavouriteRecipes(favouriteEntity: FavouriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipes(favouriteEntity)
        }

    fun deleteFavouriteRecipe(favouriteEntity: FavouriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouriteEntity)
        }

    fun deleteAllFavouriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouriteRecipes()
        }


    /**Retrofit*/
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()


    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }


     fun getFoodJoke(apiKey: String) =viewModelScope.launch {
         getFoodJokeSafeCall(apiKey) }



    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)

    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "You reach this state")
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes Not Found.")
            }

        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                Log.d(TAG, "You reach this state")
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)

                //    offlineCacheRecipes(foodRecipe)

           } catch (e: Exception) {
               recipesResponse.value = NetworkResult.Error("Recipes Not Found.")
            }

        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getGoodFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)
                val foodJoke = foodJokeResponse.value!!.data

                if (foodJoke !=null){
                    offLineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes Not Found.")
            }
        }
        else{
            foodJokeResponse.value=NetworkResult.Error("No Internet Connection")
        }
    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offLineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }


    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }

        }

    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")

            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

