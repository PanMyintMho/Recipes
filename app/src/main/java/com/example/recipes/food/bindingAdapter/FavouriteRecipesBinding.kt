package com.example.recipes.food.bindingAdapter

import android.view.View

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.food.adapters.FavouriteRecipesAdapter
import com.example.recipes.food.data.database.entity.FavouriteEntity

class FavouriteRecipesBinding {
    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favouriteEntity: List<FavouriteEntity>?,
            mAdapter: FavouriteRecipesAdapter?
        ) {

            when (view) {
                is RecyclerView -> {
                    val checkData = favouriteEntity.isNullOrEmpty()
                    view.isInvisible = checkData

                    if (!checkData) {
                        favouriteEntity?.let {
                            mAdapter?.setData(it)
                        }
                    }
                }

                else -> view.isVisible = favouriteEntity.isNullOrEmpty()
            }


        }

    }
}