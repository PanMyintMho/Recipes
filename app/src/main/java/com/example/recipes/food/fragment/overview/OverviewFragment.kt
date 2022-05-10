package com.example.recipes.food.fragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.FragmentOverviewBinding
import com.example.recipes.food.bindingAdapter.RecipesRowBinding
import com.example.recipes.food.models.Result
import com.example.recipes.food.util.Constants.Companion.RECIPES_RESULT_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private  var _binding: FragmentOverviewBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPES_RESULT_KEY) as Result
        binding.mainImageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.clockTextView.text = myBundle.readyInMinutes.toString()
        RecipesRowBinding.parseHtml(binding.summarTextView, myBundle.summary)


        updateColor(myBundle.vegetarian,binding.vegetarianTextView,binding.vegetarianImageView)
        updateColor(myBundle.vegan,binding.veganTextView,binding.veganImageView)
        updateColor(myBundle.cheap,binding.cheapTextView,binding.cheapImageView)
        updateColor(myBundle.dairyFree,binding.dairyTextView,binding.dairyImageView)
        updateColor(myBundle.glutenFree,binding.glutenTextView,binding.glutenImageView)
        updateColor(myBundle.veryHealthy,binding.healthTextView,binding.healthImageView)



        return binding.root
    }

    private fun updateColor(stateIsOn :Boolean,textView:TextView,imageView:ImageView){
       if (stateIsOn){
           imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))

       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }
}