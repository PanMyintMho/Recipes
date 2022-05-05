package com.example.recipes.food.util

class Constants {

    companion object {
        const val BASE_URl = "https://api.spoonacular.com"
        const val API_KEY = "dff7d7d9c3b344e1ae0919be84d1ecca"
        const val BASE_IMAGE_RUL="https://spoonacular.com/cdn/ingredients_100x100/"

        const val RECIPES_RESULT_KEY="recipeBundle"
        //API Query Key

        const val QUERY_SEARCH="query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGEREDIENTS = "fillIngredients"

        //ROOM Database
        const val DATABASE_NAME="recipes_database"
        const val RECIPES_TABLE="recipes_table"
        const val FAVOURITE_RECIPES_TABLE="favourite_recipes_table"
        const val FOOD_JOKE_TABLE="food_joke_table"


        //Bottoms Sheet and Preference
        const val DEFAULT_RECIPES_NUMBER="50"
        const val DEFAULT_MEAL_TYPE="main course"
        const val DEFAULT_DIET_TYPE="gluten free"

        const val PREFERENCE_NAME="foody_preferences"
        const val PREFERENCE_MEAL_TYPE="mealType"
        const val PREFERENCE_MEAL_TYPE_ID="mealTypeId"
        const val PREFERENCE_DIET_TYPE="dietType"
        const val PREFERENCE_DIET_TYPE_ID="dietTypeId"


        const val PREFERENCES_BACK_ONLINE="backOnline"

    }
}