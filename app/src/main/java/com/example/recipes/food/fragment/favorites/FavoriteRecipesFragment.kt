package com.example.recipes.food.fragment.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.databinding.FragmentFavouriteBinding
import com.example.recipes.food.adapters.FavouriteRecipesAdapter
import com.example.recipes.food.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavouriteRecipesAdapter by lazy {
        FavouriteRecipesAdapter(
            requireActivity(),
            mainViewModel
        )
    }
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter
        setHasOptionsMenu(true)
        setUpRecyclerView(binding.favouriteRecycler)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_favourite_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     if (item.itemId == R.id.delete_all_favourite_menu){
         mainViewModel.deleteAllFavouriteRecipes()
         showSnackBar()
     }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
    private fun showSnackBar(){
        Snackbar.make(
            binding.root,
            "All recipes removed",
            Snackbar.LENGTH_SHORT)
            .setAction("Okay"){}
            .show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearDeleteActionMode()
    }
}