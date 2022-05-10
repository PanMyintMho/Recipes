package com.example.recipes.food.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.IngredientRowBinding
import com.example.recipes.food.models.ExtendedIngredient
import com.example.recipes.food.util.Constants.Companion.BASE_IMAGE_RUL
import com.example.recipes.food.util.RecipesDiffUtil
import java.util.*

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {
    private var ingredientList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = IngredientRowBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.ingredientImage.load(BASE_IMAGE_RUL + ingredientList[position].image){
            crossfade(600)
            error(R.drawable.ic_baseline_error_24)
        }
        holder.binding.ingredientName.text = ingredientList[position].name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        holder.binding.ingredientAmount.text = ingredientList[position].amount.toString()
        holder.binding.ingredientUnit.text = ingredientList[position].unit
        holder.binding.ingredientConsistency.text = ingredientList[position].consistency
        holder.binding.ingredientOriginal.text = ingredientList[position].original


    }
    override fun getItemCount(): Int {
        return ingredientList.size
    }


    fun setData(newIngredients :List<ExtendedIngredient>){
        val ingredientDiffutil = RecipesDiffUtil(ingredientList,newIngredients)
        val diffutilResult = DiffUtil.calculateDiff(ingredientDiffutil)
        ingredientList = newIngredients
        diffutilResult.dispatchUpdatesTo(this)
    }

}








