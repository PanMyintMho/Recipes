package com.example.recipes.food.fragment.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.preferences.protobuf.ExperimentalApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipes.R
import com.example.recipes.databinding.FragmentRecipesBinding
import com.example.recipes.food.adapters.RecipesAdapter
import com.example.recipes.food.util.Constants.Companion.API_KEY
import com.example.recipes.food.util.Constants.Companion.QUERY_SEARCH
import com.example.recipes.food.util.NetworkListener
import com.example.recipes.food.util.NetworkResult
import com.example.recipes.food.util.observeOnce
import com.example.recipes.food.viewModel.MainViewModel
import com.example.recipes.food.viewModel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    // private val mainViewModel: MainViewModel by viewModels()
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var networkListener: NetworkListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)
        setUpRecyclerView()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checknetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipeFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }
        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("ReciFragment", "readDatabase Called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }
    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipes = response.data
                    foodRecipes?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }



    private fun applyQueries(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }

    private fun setUpRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }


    private fun showShimmerEffect() {
        binding.shimmerframe.visibility = View.VISIBLE
        binding.shimmerframe.startShimmer()
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerframe.hideShimmer()
        binding.shimmerframe.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}


