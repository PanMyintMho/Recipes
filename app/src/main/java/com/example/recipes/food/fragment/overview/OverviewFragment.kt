package com.example.recipes.food.fragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.FragmentOverviewBinding
import com.example.recipes.food.models.Result
import com.example.recipes.food.util.Constants.Companion.RECIPES_RESULT_KEY
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPES_RESULT_KEY)
        binding.mainImageView.load(myBundle?.image)
        binding.titleTextView.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.clockTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary= it?.let { it1 -> Jsoup.parse(it1).text() }
            binding.summarTextView.text = summary
        }


        if (myBundle?.vegetarian == true) {
            binding.vegetarianImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.vegetarianTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )

        }
        if (myBundle?.vegan == true) {
            binding.veganImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.veganTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.glutenFree == true) {
            binding.glutenImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.glutenTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.dairyFree == true) {
            binding.dairyImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.dairyTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        if (myBundle?.veryHealthy == true) {
            binding.healthImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.healthTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }


        if (myBundle?.cheap == true) {
            binding.cheapImageView.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.cheapTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        return binding.root


    }
}