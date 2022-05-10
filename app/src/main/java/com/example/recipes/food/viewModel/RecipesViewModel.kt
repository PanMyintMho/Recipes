package com.example.recipes.food.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.food.data.DataStoreRepository
import com.example.recipes.food.data.MealAndDietType
import com.example.recipes.food.util.Constants.Companion.API_KEY
import com.example.recipes.food.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.recipes.food.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipes.food.util.Constants.Companion.QUERY_API_KEY
import com.example.recipes.food.util.Constants.Companion.QUERY_DIET
import com.example.recipes.food.util.Constants.Companion.QUERY_FILL_INGEREDIENTS
import com.example.recipes.food.util.Constants.Companion.QUERY_NUMBER
import com.example.recipes.food.util.Constants.Companion.QUERY_SEARCH
import com.example.recipes.food.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {
    private lateinit var mealAndDietType: MealAndDietType
    var networkStatus = false
    var backOnline = false
    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    var readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealAndDietType.selectedMealType,
                mealAndDietType.selectedMealTypeId,
                mealAndDietType.selectedDietType,
                mealAndDietType.selectedDietTypeId
            )

        }


    fun saveMealAndDietTypeTemp(
        mealType: String, mealTypeId: Int,
        dietType: String, dietTypeId: Int
    ) {
        mealAndDietType = MealAndDietType(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId
        )
    }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)

        }


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealAndDietType.selectedMealType
        queries[QUERY_DIET] = mealAndDietType.selectedDietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGEREDIENTS] = "true"
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGEREDIENTS] = "true"
        return queries
    }


    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}