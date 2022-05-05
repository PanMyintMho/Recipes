package com.example.recipes.food.bindingAdapter

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.datastore.dataStore
import com.example.recipes.food.data.database.entity.FoodJokeEntity
import com.example.recipes.food.models.FoodJoke
import com.example.recipes.food.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {
    companion object {
        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic

        fun setCardAndProgressVisibility(
            view: View,
            readApiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {

            when (readApiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }

                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE

                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                else -> {

                }
            }

        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewVisibility(
            view: View, apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (database != null) {
                if (database.isEmpty()) {
                    view.visibility = View.VISIBLE

                    if (view is TextView) {
                        if (apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }
            if (apiResponse is NetworkResult.Success) {
                view.visibility = View.INVISIBLE
            }
        }

    }
}