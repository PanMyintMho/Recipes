package com.example.recipes.food.fragment.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.databinding.FragmentIngredientBinding
import com.example.recipes.food.adapters.IngredientAdapter
import com.example.recipes.food.models.Result
import com.example.recipes.food.util.Constants.Companion.RECIPES_RESULT_KEY


class IngredientFragment : Fragment() {


    private  var _binding: FragmentIngredientBinding?= null
    private val binding get() = _binding!!

    private val mAdapter:IngredientAdapter by lazy { IngredientAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
       val args= arguments
        val myBundle : Result? = args?.getParcelable(RECIPES_RESULT_KEY)!!
        setupRecyclerView()
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
        return binding.root
    }

    private fun setupRecyclerView(){
        binding.ingredientRecycler.adapter = mAdapter
        binding.ingredientRecycler.layoutManager=LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}