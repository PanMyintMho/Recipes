package com.example.recipes.food.adapters

import android.annotation.SuppressLint
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.databinding.FavouriteRecipesRowBinding
import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.fragment.favorites.FavoriteRecipesFragmentDirections
import com.example.recipes.food.util.RecipesDiffUtil
import com.example.recipes.food.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {


    private lateinit var rootView: View
    private lateinit var mActionMode: ActionMode
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavouriteEntity>()
    private var myViewHolder = arrayListOf<MyViewHolder>()
    private var favouriteRecipes = emptyList<FavouriteEntity>()


    class MyViewHolder(val binding: FavouriteRecipesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteEntity: FavouriteEntity) {
            binding.favouritesEntity = favouriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRecipesRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolder.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)

        saveItemStateOnScroll(currentRecipe,holder)

        /**
         * Single Click Listener
         **/

        holder.binding.favouriteRecipeRowLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipe)

            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavouriteFragmentToDetailActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)

            }
        }

        /**
         * Long Click Listener
         */

        holder.binding.favouriteRecipeRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                applySelection(holder,currentRecipe)
                true

            }

        }

    }

    @SuppressLint("PrivateResource")
    private fun saveItemStateOnScroll(currentRecipe: FavouriteEntity,holder:MyViewHolder){
        if (selectedRecipes.contains(currentRecipe)) {
            changeRecipeStyle(
                holder, R.color.cardBackgroundLightColor,
                com.google.android.material.R.color.design_dark_default_color_primary)

        } else {

            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)

        }

    }



    @SuppressLint("PrivateResource")
    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(
                holder, R.color.cardBackgroundLightColor,
                com.google.android.material.R.color.design_dark_default_color_primary
            )
            applyActionModeTitle()

        }

    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {

        holder.binding.favouriteRecipeRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.binding.favouriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)

    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"

            }
        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {

        actionMode?.menuInflater?.inflate(R.menu.favourite_delete_menu, menu)

        mActionMode = actionMode!!
        applyStatusBarColor(R.color.deleteStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {

        if (menu?.itemId == R.id.delete_favourite_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipes reomved.")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }

        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {

        myViewHolder.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)

        }

        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavouriteRecipe: List<FavouriteEntity>) {

        val favouriteRecipesDiffUtil = RecipesDiffUtil(favouriteRecipes, newFavouriteRecipe)
        val diffUtilResult = DiffUtil.calculateDiff(favouriteRecipesDiffUtil)

        favouriteRecipes = newFavouriteRecipe
        diffUtilResult.dispatchUpdatesTo(this)

    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction("Okay") {}
            .show()

    }

    fun clearDeleteActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}