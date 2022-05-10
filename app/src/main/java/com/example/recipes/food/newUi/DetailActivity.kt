package com.example.recipes.food.newUi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.recipes.R
import com.example.recipes.databinding.ActivityDetailBinding
import com.example.recipes.food.adapters.PagerAdapter
import com.example.recipes.food.data.database.entity.FavouriteEntity
import com.example.recipes.food.fragment.ingredients.IngredientFragment
import com.example.recipes.food.fragment.instruction.InstructionFragment
import com.example.recipes.food.fragment.overview.OverviewFragment
import com.example.recipes.food.util.Constants.Companion.RECIPES_RESULT_KEY
import com.example.recipes.food.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private var saveRecipeId = 0
    private val args by navArgs<DetailActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSave = false
    private lateinit var binding: ActivityDetailBinding
    private lateinit var menuItem:MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPES_RESULT_KEY, args.result)
        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )


        binding.viewPager2.isUserInputEnabled =false
        binding.viewPager2.apply {
            adapter=pagerAdapter
        }

       TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab,position ->
           tab.text = titles[position]
       }.attach()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.detail_menu, menu)
         menuItem = menu!!.findItem(R.id.save_to_favourite_menu)
        checkSaveRecipes(menuItem)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourite_menu && !recipeSave) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.save_to_favourite_menu && recipeSave) {
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {

        val favouriteEntity = FavouriteEntity(0, args.result)
        mainViewModel.insertFavouriteRecipes(favouriteEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipes Saved .")
        recipeSave = true

    }

    private fun checkSaveRecipes(menuItem: MenuItem) {
        mainViewModel.readFavourideRecipes.observe(this, { favouriteEntity ->
            try {
                for (saveRecipes in favouriteEntity) {
                    if (saveRecipes.result.recipesId == args.result.recipesId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        saveRecipeId = saveRecipes.id
                        recipeSave = true
                    }
                }
            } catch (e: Exception) {
                Log.d("Detail Activity", e.message.toString())
            }

        })
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouriteEntity =
            FavouriteEntity(
                saveRecipeId, args.result
            )
        mainViewModel.deleteFavouriteRecipe(favouriteEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favourite.")
        recipeSave = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailLayout, message, Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem,R.color.white)
    }

}